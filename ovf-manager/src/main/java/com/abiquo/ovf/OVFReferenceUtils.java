/**
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * The Original Code is available at https://abicloud.svn.sourceforge.net/svnroot/abicloud
 *
 * The Initial Developer of the Original Code is Soluciones Grid, S.L. (www.abiquo.com),
 * Consell de Cent 296 principal 2º, 08007 Barcelona, Spain.
 * No portions of the Code have been created by third parties.
 * All Rights Reserved.
 *
 * Contributor(s): ______________________________________.
 *
 * Graphical User Interface of this software may be used under the terms
 * of the Common Public Attribution License Version 1.0 (the  "CPAL License",
 * available at http://cpal.abiquo.com), in which case the provisions of CPAL
 * License are applicable instead of those above. In relation of this portions
 * of the Code, a Legal Notice according to Exhibits A and B of CPAL Licence
 * should be provided in any distribution of the corresponding Code to Graphical
 * User Interface.
 */


package com.abiquo.ovf;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.dmtf.schemas.ovf.envelope._1.EnvelopeType;
import org.dmtf.schemas.ovf.envelope._1.FileType;
import org.dmtf.schemas.ovf.envelope._1.ReferencesType;

import com.abiquo.ovf.exceptions.IdAlreadyExistsException;
import com.abiquo.ovf.exceptions.IdNotFoundException;

public class OVFReferenceUtils
{
    /*******************************************************************************
     * FILE REFERENCES SECTION
     *******************************************************************************/

    /**
     * Creates a new FileType object.
     * 
     * @param fileId, the required file identifier.
     * @param href, the required file reference, can be a local relative path or http/ftp/s3/dfs
     *            URL.
     * @param fileSize, the required file size. TODO will be cool try to infer the size if not
     *            provided.
     * @param compression, the optional compression on the file ("gzip" or "bzip2" )
     * @param chunkSize, the optional file chunk size for partitioned files. (look at the standard
     *            fore more info)
     */
    public static FileType createFileType(String fileId, String href, BigInteger fileSize,
        String compresion, Long chunkSize)
    {
        FileType fileRef = new FileType(); // = envelopFactory.createFileType();

        fileRef.setSize(fileSize); // TODO assert not null
        fileRef.setHref(href); // TODO assert not null and protocol
        fileRef.setId(fileId); // TODO assert not null

        if (chunkSize != null)
        {
            fileRef.setChunkSize(chunkSize);
        }
        if (compresion != null)
        {
            // TODO ("gzip" or "bzip2" )
            fileRef.setCompression(compresion);
        }

        return fileRef;
    }

    /**
     * Adds a File to the OVF envelope's Reference element.
     * 
     * @param fileRed, the file reference to be added.
     * @param reference, the ReferencesType to be changed.
     * @throws IdAlreadyExistsException if the provided File id is already on the Envelope's
     *             ReferencesSection.
     */
    public static void addFile(ReferencesType references, FileType fileRef) throws IdAlreadyExistsException
    {
        for (FileType file : references.getFile())
        {
            if (file.getId().equalsIgnoreCase(fileRef.getId()))
            {
                final String msg =
                    "The File fileId" + fileRef.getId() + " already on ReferencesSection";
                throw new IdAlreadyExistsException(msg);
            }
        }

        references.getFile().add(fileRef);
    }

    /**
     * Change the location (FileType.hRef ) of an existing file on the Envelope's ReferenceSection.
     * It assumes the file has the same size, and other attributes (TODO).
     * 
     * @param envelop, the target VirtualSystem's envelope.
     * @param fileId, an existing FileType id on the envelope ReferenceSection.
     * @param newHRef, the new location for this file.
     * @throws IdNotFoundException if fileId is not on the Envelope's ReferenceSection.
     */
    public static void changeFileLocation(EnvelopeType envelop, String fileId, String newHRef)
        throws IdNotFoundException
    {
        boolean found = false;
        for (FileType file : envelop.getReferences().getFile())
        {
            if (fileId.equals(file.getId()))
            {
                found = true;
                file.setHref(newHRef);
            }
        }

        if (!found)
        {
            final String msg = "The file id " + fileId + " not present on ReferencesSection";
            throw new IdNotFoundException(msg);
        }
    }

    /**
     * Gets all the referenced files on an OVF envelope.
     * 
     * @param envelop, the OVF envelope to be inspected.
     * @return all the file locations on the ReferencesSection for the given envelope.
     */
    public static Set<String> getAllReferencedFileLocations(EnvelopeType envelop)
    {
        Set<String> fileRefs = new HashSet<String>();

        for (FileType file : envelop.getReferences().getFile())
        {
            fileRefs.add(file.getHref());
        }

        return fileRefs;
    }

    /**
     * Gets all the referenced files on an OVF envelope.
     * 
     * @param envelop, the OVF envelope to be inspected.
     * @return all the file locations on the ReferencesSection for the given envelope. TODO is
     *         useful ??
     *         
     *         @deprecated unusefull
     */
    public static Set<FileType> getAllReferencedFile(EnvelopeType envelope)
    {
        Set<FileType> files = new HashSet<FileType>();

        files.addAll(envelope.getReferences().getFile());

        return files;
    }

    /**
     * Gets all the referenced files on an OVF envelope.
     * 
     * @param envelop, the OVF envelope to be inspected.
     * @return all the file locations on the ReferencesSection for the given envelope.
     * @throws IdNotFoundException, if the desired file id is not present on the envelope
     */
    public static FileType getReferencedFile(EnvelopeType envelope, String fileId)
        throws IdNotFoundException
    {
        for (FileType f : envelope.getReferences().getFile())
        {
            if (fileId.equals(f.getId()))
            {
                return f;
            }
        }
        throw new IdNotFoundException("FileId: " + fileId);
    }
    
}
