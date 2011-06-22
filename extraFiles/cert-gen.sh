#!/bin/bash -x

echo "Generate CA certificate and signed user certificate."

# Parameters.
user_password=jettycred
ca_password=stratuslabcapswd

# Clean up from any previous runs.
rm -f *.cfg
rm -f *.csr
rm -f *.pem
rm -f *.p12
rm -Rf dist
rm -Rf demoCA

# Create directory structure.
mkdir -p demoCA
mkdir -p demoCA/private
mkdir -p demoCA/newcerts
touch demoCA/index.txt
touch demoCA/private/.rand
cat <<EOF > demoCA/serial
01
EOF
cat <<EOF > demoCA/crlnumber
01
EOF
mkdir -p dist
mkdir -p user

echo "Creating CA certificate..."

cat > ca.cfg <<EOF
[ req ]
distinguished_name     = req_distinguished_name
x509_extensions        = v3_ca
prompt                 = no
input_password         = ${ca_password}
output_password        = ${ca_password}

dirstring_type         = nobmp

[ ca ]
default_ca     = CA_default            # The default ca section

[ CA_default ]
dir            = ./demoCA              # top dir
database       = \$dir/index.txt        # index file.
new_certs_dir  = \$dir/newcerts         # new certs dir

certificate    = \$dir/cacert.pem       # The CA cert
serial         = \$dir/serial           # serial no file
private_key    = \$dir/private/cakey.pem# CA private key
RANDFILE       = \$dir/private/.rand    # random number file

default_days   = 5                     # how long to certify for
default_crl_days= 2                    # how long before next CRL
default_md     = md5                   # md to use

policy         = policy_any            # default policy
email_in_dn    = no                    # Don't add the email into cert DN

name_opt       = ca_default            # Subject name display option
cert_opt       = ca_default            # Certificate display option
copy_extensions = none                 # Don't copy extensions from request

[ policy_any ]
countryName            = supplied
stateOrProvinceName    = optional
organizationName       = optional
organizationalUnitName = optional
commonName             = supplied
emailAddress           = optional

[ req_distinguished_name ]
C = EU
O = StratusLab
OU = Test
CN = TestCA

[ v3_ca ]
basicConstraints = CA:true
nsCertType=client, email, objsign
keyUsage=critical, keyCertSign, cRLSign
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid:always,issuer:always

[ v3_ca_user ]
basicConstraints = CA:false
nsCertType=client, email, objsign
keyUsage=critical, digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid:always,issuer:always


EOF

cat > openssl.cfg <<EOF
[ req ]
distinguished_name     = req_distinguished_name
x509_extensions        = v3_ca
prompt                 = no
input_password         = ${user_password}
output_password        = ${user_password}

dirstring_type = nobmp

[ req_distinguished_name ]
C = EU
O = StratusLab
OU = Test
CN = John Smith

[ v3_ca ]
basicConstraints = CA:true
nsCertType=client, email, objsign
keyUsage=critical, digitalSignature, nonRepudiation, keyEncipherment, dataEncipherment
subjectKeyIdentifier=hash
authorityKeyIdentifier=keyid:always,issuer:always

EOF

# Generate the root CA certificate and key.
openssl req \
            -x509 \
            -newkey rsa:1024 \
            -keyout demoCA/private/cakey.pem \
            -out demoCA/cacert.pem \
            -config ca.cfg

chmod 0600 demoCA/private/cakey.pem

# Generate a Certificate Revocation List (CRL).
openssl ca \
           -gencrl \
           -out crl.pem \
           -passin pass:${ca_password} \
           -config ca.cfg

# Generate the hash value of the certificate.
cahash=`openssl x509 -noout -subject_hash_old -in demoCA/cacert.pem`

# Generate signing policies.
cat > dist/stratuslab-test-ca.namespaces <<'EOF'
TO Issuer "/C=EU/O=StratusLab/OU=Test/CN=TestCA" PERMIT Subject "/C=EU/O=StratusLab/OU=Test/.*"
EOF

cat > dist/stratuslab-test-ca.signing_policy <<'EOF'
 access_id_CA   X509    '/C=EU/O=StratusLab/OU=Test/CN=TestCA'
 pos_rights     globus  CA:sign
 cond_subjects  globus  '"/C=EU/O=StratusLab/OU=Test/*"'
EOF

# Create CA 'distribution'.
cd dist
cp ../demoCA/cacert.pem stratuslab-test-ca.pem
ln -s stratuslab-test-ca.pem $cahash.0

mv ../crl.pem stratuslab-test-ca.crl
ln -s stratuslab-test-ca.crl $cahash.r0

ln -s stratuslab-test-ca.namespaces $cahash.namespaces

ln -s stratuslab-test-ca.signing_policy $cahash.signing_policy

cd -

# Generate initial private key.
#openssl genrsa \
#               -out user/stratuslab-test-user-key.pem \
#               -des3 \
#               -passout pass:${user_password} \
#               2048

openssl genrsa \
               -out user/stratuslab-test-user-key.pem \
               2048

chmod 0600 user/stratuslab-test-user-key.pem

# Create a certificate signing request.
openssl req \
            -out user/stratuslab-test-user.csr \
            -new \
            -key user/stratuslab-test-user-key.pem \
            -passin pass:${user_password} \
            -config openssl.cfg

# Sign the certificate request.
openssl ca \
           -in user/stratuslab-test-user.csr \
           -out user/stratuslab-test-user-cert.pem \
           -extensions v3_ca_user \
           -passin pass:${ca_password} \
           -config ca.cfg <<EOF
y
y
EOF

# Convert to PKCS12 format. 
openssl pkcs12 -export \
               -in user/stratuslab-test-user-cert.pem \
               -inkey user/stratuslab-test-user-key.pem \
               -out user/stratuslab-test-user.p12 \
               -passin pass:${user_password} \
               -passout pass:${user_password}

# Remove intermediate files.
rm -f *.cfg user/*.csr

# Remove existing certificate files.
certdir='/etc/grid-security/certificates'
oldcert="${certdir}/stratuslab-test-ca.pem"
if [ -e ${oldcert} ]; then
  oldhash=`openssl x509 -noout -hash -in ${oldcert}`
  sudo su - root -c "rm -f ${certdir}/${oldhash}.*"
  sudo su - root -c "rm -f ${certdir}/stratuslab-test-ca.*"
fi

echo "Copy CA certificate and policies into place."
pwd
sudo su - root -c "mkdir -p ${certdir}"
sudo su root -c "cp -d dist/* ${certdir}"

echo "Restart one-proxy to ensure that new certs are taken into account immediately."
sudo su - root -c "service one-proxy restart"

# Save user certificate information. 
sudo su - root -c 'rm -Rf /tmp/stratuslab-test-user' 
mkdir -p /tmp/stratuslab-test-user
cp user/* /tmp/stratuslab-test-user

