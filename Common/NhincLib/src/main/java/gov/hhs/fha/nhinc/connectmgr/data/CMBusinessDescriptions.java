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

package gov.hhs.fha.nhinc.connectmgr.data;

import java.util.List;
import java.util.ArrayList;

/**
 * This class contains one or more descriptions for the business entity.
 * 
 * @author Les Westberg
 */
public class CMBusinessDescriptions
{
    private List<String> businessDescriptionList = new ArrayList<String>();

    /**
     * Default Constructor.
     */
    public CMBusinessDescriptions()
    {
        clear();
    }

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        businessDescriptionList = new ArrayList<String>();
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessDescriptions oCompare)
    {
        if (oCompare.businessDescriptionList.size() != this.businessDescriptionList.size())
        {
            return false;
        }
        
        int iCnt = this.businessDescriptionList.size();
        for (int i = 0; i < iCnt; i++)
        {
            if (! this.businessDescriptionList.get(i).equals(oCompare.businessDescriptionList.get(i)))
            {
                return false;
            }
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }

    /**
     * Returns the list of business descriptions for this entity.
     * 
     * @return The list of business descriptions for this entity.
     */
    public List<String> getBusinessDescription()
    {
        return businessDescriptionList;
    }
    
    
}