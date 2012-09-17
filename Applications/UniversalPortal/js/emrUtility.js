/*
 * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

var decisionTree = {
    container:null,
    dataObj:null,
    actionsPerRow:5,
    init: function(data, container) {
        this.container=container;
        this.dataObj=data;
        container.delegate('.emrDiagActionClickable', 'click', function() {
            $.publish( '/dxGuide/actionDetail', [ $(this) ] );
            return false;
        });
        this.drawEntire(data);
        this.container.attr({ scrollTop: this.container.attr("scrollHeight") });    // Start the guide at the bottom
    },
    drawEntire: function(data) {
        // Clear the container and redraw the entire diagram
        this.container.empty();
        // Iterate over all of the stages or decisions of the guide
        $.each(data.diagnosticProcessHistory, function() {
            decisionTree.drawDecision(this);
        });
    },
    drawDecision: function(decision){
        // Get the severity color based on the decision severity
        var _severityIdx = sirona.providerTemplate.dxGuideStageSeverities.indexOf(decision.severity);
        var _severityColor = sirona.providerTemplate.predictiveSeverityColors[_severityIdx];
        if (decision.current) $.publish( '/emr/severityLevel',[ _severityIdx, _severityColor  ] );

        // Create the Stage node, style the severity color for it, then append the Other Options <div>
        var _thisStage = '<div id="dxGuideStage-' + decision.nodeId + '" class="emrDiagStage' + ((!decision.current)?' emrDiagStageCompleted':'') + '">' +
            '<table class="tblClean emrDiagStageNode" style="border:2px solid ' + _severityColor + '"><tr>' +
                '<td class="emrDiagStageHeader" style="background:' + _severityColor + '">Stage ' + decision.stage + '</td>' +
                '<td>' + decision.response + '</td><td><div class="emrDiagOtherOptions emrDiagActionContent' + ((decision.current)?' emrDiagActionClickable':'') + '">Other Diagnostic Choices</div></td></tr></table>' +
            '<div class="emrDiagStageArrow" style="background-color:' + _severityColor + '"></div>' +
            this.getActions(decision.actions, decision.current) + '</div>';

        this.container.append(_thisStage);  // End the stage division
        var _stageNode = $('#dxGuideStage-' + decision.nodeId );

        // Store the Action metadata inside the Action node tag for later use (like getting a survey, or displaying the status box below the last decision)
        var _lowOptions=[], _otherOptions=[];
        $.each(decision.actions, function() {
            if (this.status.toLowerCase() == 'not started') {
                if (!this.utilityLevel || !this.utility || parseFloat(this.utility)==0 ) _otherOptions.push(this);
                else if (this.utilityLevel=='Low') _lowOptions.push(this);
                else if ( $('#'+this.actionId).length > 0 ) $('#'+this.actionId).data(this);
            }
                else if ( $('#'+this.actionId).length > 0 ) $('#'+this.actionId).data(this);
        });
        $('.emrDiagOtherOptions', _stageNode).data(_otherOptions);  // Store the object data array into the Other options, to be accessed when clicked (delegated)
        $('.emrDiagActionNodeLow', _stageNode).data(_lowOptions);   // Store the object data array into the Low Utility options, to be accessed when clicked (delegated)

        this.drawLines(_stageNode, decision );
    },
    getActions: function(decisionActions, current) {
        if (decisionActions.length==0) return;
        var _actionsPerRow = this.actionsPerRow ;

        var _spacerPerc = 100 / ((decisionActions.length > _actionsPerRow) ? _actionsPerRow : decisionActions.length);
        var _spacer = '<td style="width:10%;min-width:75px;">&nbsp;</td>';
        var _actionRow = '<table class="tblClean emrDiagActionRow" style="width:100%"><tr>';
        var _actionNodes = _actionRow;
        var _actionNodeHeadingClass='', _actionNodeClass='';  // To add styling classes to the main loop
        var _optionLabelCnt = 0, _lowUsed = false;
        var _actionHeading='', _actionContent = '';
        // Loop through and display all actions for a decision
        $.each(decisionActions, function(idxOption, decisionAction) {
            if (_optionLabelCnt > 8) return false;   // Ignore extra actions if passed in !
            if (decisionAction.utility && parseFloat(decisionAction.utility) == 0) return true;  // Skip to the next item if no utility (will be shown in Other Options)

            // Style the colors for all the actions
            _actionNodeClass = 'emrDiagActionNode';                         // Default node shape
            if (current) _actionNodeClass += ' emrDiagActionClickable';     // Clickable only if the stage is the current on (passed in)
            _actionNodeHeadingClass = 'emrDiagActionNodeHeading';           // Default node heading size and text color
            if (decisionAction.utilityLevel == "Low") {
                if (_lowUsed) return;
                _actionNodeClass += ' emrDiagActionNodeLow';
                _actionNodeHeadingClass += ' suiteGrayBG';
            } else if (decisionAction.utilityLevel == 'Suggested') {
                _actionNodeClass += ' emrDiagActionNodeSuggested';
                _actionNodeHeadingClass += ' emrDiagSuggestedBG';
            } else {
                _actionNodeClass += ' emrDiagActionNodeNormal';
                _actionNodeHeadingClass += ' primaryBG';
            }

            // Use the next row if the maximum actions per row is reached
            if (_optionLabelCnt == _actionsPerRow) {
                // If this stage needs another row of actions, write the 1st and create a new row with a spacer
                _actionNodes += '</tr></table>' + _actionRow + _spacer;
            }

            _optionLabelCnt++;
            _actionHeading = 'Option ' + _optionLabelCnt;
            _actionContent = decisionAction.descr;
            if (decisionAction.utilityLevel == 'Low') {
                _actionHeading=''; _actionContent='Low Utility Options'; _lowUsed=true
            } else if (decisionAction.status != 'Not Started') _actionNodeClass += ' lightBG';     // Shade the background if its anything other than Not Started

            // Write the node output
            _actionNodes += '<td style="width:'+ _spacerPerc + '%;min-width:150px;"><div style="position:relative">' +
                '<div class="' + _actionNodeClass + '" id="' + decisionAction.actionId + '">';
                     if (decisionAction.utilityLevel != 'Low') _actionNodes += '<div class="' + _actionNodeHeadingClass + '">' + _actionHeading + '</div>';
                    _actionNodes += '<div class="emrDiagActionContent">' + _actionContent + '</div>' +
                '</div>';
            if (decisionAction.status != 'Not Started')
                _actionNodes += '<div class="dxDiagActionStatusText">' + decisionAction.status + ': ' + decisionAction.statusUpdated +  '</div>';
            _actionNodes += "</div></td>\n";
        });
        if (_optionLabelCnt > _actionsPerRow) {
            // Create empty nodes to make sure spacing is right
            for (var i=9-_optionLabelCnt; i>0 ;i--) { _actionNodes += ('<td style="width:' + _spacerPerc + '%;min-width:150px;"><div style="position:relative"></div></td>') }
            _actionNodes += _spacer;
        }
        return _actionNodes + '</tr></table>';
    },
    drawLines: function( stageContainer, decision ) {
        var _actionsPerRow = this.actionsPerRow ;

        var _topOffset = $('.emrDiagActionNode').height() + 22 ;
        var _leftOffset = $('.emrDiagActionNode').width() / 2 - 2;
        var _actions = stageContainer.find('.emrDiagActionNode');   // Get all action nodes
        var _twoLevels = (_actions.length > _actionsPerRow);
        var _firstCol=9, _lastCol=2, _lastColRow=1;

        var _shortVertical = (decision.current)?'shortDiagArrow':'shortDiagLine';
        var _longVertical = (decision.current)?'longDiagArrow':'longDiagLine';
        _actions.each(function(idx) {
            if ($(this).hasClass('lightBG')) {
                // Calculate first and last columns of the action node to draw the horizontal line at the end
                var _col = ((idx >= _actionsPerRow)?(idx - _actionsPerRow + 1):idx);        // If on the second row, then add 1 to the calculation to skip over the first cell, which is for margin only
                if (_col < _firstCol) _firstCol = _col;
                if (_col > _lastCol) {_lastCol = _col; if (idx >= _actionsPerRow) _lastColRow=2 }  // Keep track of which row the last column is in

                $(this).append('<img src="images/' +  ((idx < _actionsPerRow && _twoLevels)?_longVertical:_shortVertical) + '.png" ' +
                    'style="cursor:default;position:absolute;top:' + _topOffset + 'px;left:' + _leftOffset + 'px"/>');
            }
        });

        // Now finish off the decision by drawing a horizontal line if it is not current
        if (!decision.current) {
//            if (_twoLevels) _lastCol += 1;                              // Add 1 to the column index if on the last row since the first row is a spacer.
            var _lastRow = stageContainer.find('tbody').last();
            var _cellStyle = '';

            _lastRow.find('td').each( function(idx) {                   // Loop through all the cells in the last row
                var _cellDiv = $(this).children('div').first();         // Get the first div inside the cell
                if (_cellDiv) {
                    _cellDiv.height(104);       // Set the height of the div inside the cell

                    if ( ( idx >= _firstCol) && (idx <= _lastCol) ) {
                        // Skip this cell if it does not contain an active node
                        if ( (idx == _firstCol) || ((idx == _lastCol) && _lastColRow==2) ) {
                            if (_cellDiv.find('.lightBG').length == 0) return;
                            _cellStyle = 'width:50%';
                            if (idx == _firstCol) _cellStyle += ';left:50%';
                        } else _cellStyle = 'width:100%';

                        _cellDiv.append('<div class="primaryBG emrDiagHorizontal" style="' + _cellStyle + '"></div>')
                    }
                }
            });
            // Put the last down arrow in
            _lastRow.append('<tr><td colspan="6" style="text-align:center"><img src="images/shortDiagArrow.png"/></td></tr>');
        }
    }
};
