/*
 * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *    END OF TERMS AND CONDITIONS
 */

package gov.hhs.fha.nhinc.kmr.ura;

/**
 * Common helper class for UniversalResourceAddressBean Implementation.
 *
 * @author  Jerry Goodnough
 */
public class BaseResourceAddressBean
{

    /** The Type of this Id. */
    protected EntityType entityType = EntityType.UNKNOWN;

    /**
     * @see  gov.hhs.fha.nhinc.kmr.ura.UniversalResourceAddressBean#getEntityType()
     */
    public EntityType getEntityType()
    {
        return entityType;
    }

    /**
     * @see  gov.hhs.fha.nhinc.kmr.ura.UniversalResourceAddressBean#getEntityTypeName()
     */
    public String getEntityTypeName()
    {
        return EntityTypeHelper.getTypeName(entityType);
    }

    /**
     * Return the URA String
     *
     * @return  URA of this Bean
     */
    public String getURA()
    {
        return getEntityTypeName() + "://";
    }

    /**
     * Indicates whether some other object is "equal to" this one. Two Id's are
     * equal if the URAs are equal.
     *
     * @param   obj  the reference object with which to compare.
     *
     * @return  true if this object is the same as the obj argument; false
     *          otherwise.
     */
    @Override public boolean equals(Object obj)
    {

        if (!(obj instanceof UniversalResourceAddressBean))
        {
            return false;
        }

        return getURA().compareTo(((UniversalResourceAddressBean) obj)
                .getURA()) == 0;

    }

    /**
     * Returns a hash code value for the object.
     *
     * @return  a hash code value for this object.
     */
    @Override public int hashCode()
    {
        return toString().hashCode();
    }

    /**
     * @see  java.lang.Object#toString()
     */
    @Override public String toString()
    {
        return this.getURA();
    }
}
