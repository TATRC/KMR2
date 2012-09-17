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

var urlParams = {};
(function () {
    var e,
        a = /\+/g,  // Regex for replacing addition symbol with a space
        r = /([^&=]+)=?([^&]*)/g,
        d = function (s) { return decodeURIComponent(s.replace(a, " ")); },
        q = window.location.search.substring(1);

    while (e = r.exec(q))
       urlParams[d(e[1])] = d(e[2]);
})();

// Minified SHA1 function from http://www.webtoolkit.info/javascript-sha1.html
function SHA1(msg){function rotate_left(n,s){var t4=(n<<s)|(n>>>(32-s));return t4;};function lsb_hex(val){var str="";var i;var vh;var vl;for(i=0;i<=6;i+=2){vh=(val>>>(i*4+4))&0x0f;vl=(val>>>(i*4))&0x0f;str+=vh.toString(16)+vl.toString(16);}
return str;};function cvt_hex(val){var str="";var i;var v;for(i=7;i>=0;i--){v=(val>>>(i*4))&0x0f;str+=v.toString(16);}
return str;};function Utf8Encode(string){string=string.replace(/\r\n/g,"\n");var utftext="";for(var n=0;n<string.length;n++){var c=string.charCodeAt(n);if(c<128){utftext+=String.fromCharCode(c);}
else if((c>127)&&(c<2048)){utftext+=String.fromCharCode((c>>6)|192);utftext+=String.fromCharCode((c&63)|128);}
else{utftext+=String.fromCharCode((c>>12)|224);utftext+=String.fromCharCode(((c>>6)&63)|128);utftext+=String.fromCharCode((c&63)|128);}}
return utftext;};var blockstart;var i,j;var W=new Array(80);var H0=0x67452301;var H1=0xEFCDAB89;var H2=0x98BADCFE;var H3=0x10325476;var H4=0xC3D2E1F0;var A,B,C,D,E;var temp;msg=Utf8Encode(msg);var msg_len=msg.length;var word_array=new Array();for(i=0;i<msg_len-3;i+=4){j=msg.charCodeAt(i)<<24|msg.charCodeAt(i+1)<<16|msg.charCodeAt(i+2)<<8|msg.charCodeAt(i+3);word_array.push(j);}
switch(msg_len%4){case 0:i=0x080000000;break;case 1:i=msg.charCodeAt(msg_len-1)<<24|0x0800000;break;case 2:i=msg.charCodeAt(msg_len-2)<<24|msg.charCodeAt(msg_len-1)<<16|0x08000;break;case 3:i=msg.charCodeAt(msg_len-3)<<24|msg.charCodeAt(msg_len-2)<<16|msg.charCodeAt(msg_len-1)<<8|0x80;break;}
word_array.push(i);while((word_array.length%16)!=14)word_array.push(0);word_array.push(msg_len>>>29);word_array.push((msg_len<<3)&0x0ffffffff);for(blockstart=0;blockstart<word_array.length;blockstart+=16){for(i=0;i<16;i++)W[i]=word_array[blockstart+i];for(i=16;i<=79;i++)W[i]=rotate_left(W[i-3]^W[i-8]^W[i-14]^W[i-16],1);A=H0;B=H1;C=H2;D=H3;E=H4;for(i=0;i<=19;i++){temp=(rotate_left(A,5)+((B&C)|(~B&D))+E+W[i]+0x5A827999)&0x0ffffffff;E=D;D=C;C=rotate_left(B,30);B=A;A=temp;}
for(i=20;i<=39;i++){temp=(rotate_left(A,5)+(B^C^D)+E+W[i]+0x6ED9EBA1)&0x0ffffffff;E=D;D=C;C=rotate_left(B,30);B=A;A=temp;}
for(i=40;i<=59;i++){temp=(rotate_left(A,5)+((B&C)|(B&D)|(C&D))+E+W[i]+0x8F1BBCDC)&0x0ffffffff;E=D;D=C;C=rotate_left(B,30);B=A;A=temp;}
for(i=60;i<=79;i++){temp=(rotate_left(A,5)+(B^C^D)+E+W[i]+0xCA62C1D6)&0x0ffffffff;E=D;D=C;C=rotate_left(B,30);B=A;A=temp;}
H0=(H0+A)&0x0ffffffff;H1=(H1+B)&0x0ffffffff;H2=(H2+C)&0x0ffffffff;H3=(H3+D)&0x0ffffffff;H4=(H4+E)&0x0ffffffff;}
var temp2=cvt_hex(H0)+cvt_hex(H1)+cvt_hex(H2)+cvt_hex(H3)+cvt_hex(H4);return temp2.toLowerCase();}

function renderSurvey(jsonIn, container, updateSurveyId) {
//alert('rendersurvey: '+ jsonIn.itemId);

    var _questions = '';
    var _surveyId = (jsonIn.itemId)?jsonIn.itemId:updateSurveyId;
    if (!_surveyId) return;
    // If a complete survey is being rendered, then it will have "surveyQuestions"
    // If it contains "updateQuestions", then the survey is being updated, not rendered from scratch
    if (jsonIn.surveyQuestions) {
        _questions = jsonIn.surveyQuestions;
        // Create the survey element
        container.empty().html('<ul id="' + _surveyId + '"></ul>');
    } else if (jsonIn.updateQuestions) {
        if ($('#'+updateSurveyId).length == 0) return;   // Return if the survey is not active anymore
        _questions = jsonIn.updateQuestions;
    } else return;

    // Loop though all the questions on the survey
    $.each(_questions, function() {
        var _questionId = this.itemId;
        var _inputType = (this.suggestedControl)?this.suggestedControl:'text';   // TODO - put dynamic control selection logic here
        var _answered = this.currentAnswer;
        var _currAnswer = (_answered)?this.currentAnswer:'';

        // Default to adding questions
        var _action = (this.action)?this.action.toLowerCase():'add';
        if (_action == 'delete') {
            $('#'+_questionId).remove();
            return; // Continue to next question
        }
        // If this question is disabled, then assign the attribute to this variable so it can be appended below
        var _disabled = (_action=='disabled')?' disabled="disabled"':'';

        // Start the question
        var _display = '';

        // Loop through and create all the possible input values for an element
        if (!this.possibleAnswers) {
            // Display a default text input box for questions that do NOT have possible answers
            _display += '<input style="height:18px;width:150px;border: 1px solid #cccccc" ' + _disabled + ' type="text" value="' + _currAnswer + '"/>';
        } else {
            // Loop through and create the control with all the possible answers
            $.each(this.possibleAnswers, function() {
                if (_inputType=='button') {
                    _display += '<button value="' + this.value + '"' + _disabled + '>' + this.label + '</button>';
                } else if (_inputType=='combobox') {
                    _display += '<option value="' + this.value + '"' + ((this.value == _currAnswer)?' selected':'') + '>' + this.label + '</option>';
                } else {
                    _display += '<span style="padding-right:10px"><input name="' + _questionId + '"' + _disabled + ' type="' + _inputType + '" value="' + this.value + '"';
                    if ( (_inputType == 'radio' && this.value == _currAnswer) | (_inputType == 'checkbox' && _currAnswer.indexOf(this.value) >= 0 ) )
                        _display += ' checked';
                    _display += ' style="margin-right:5px;">' + this.label + '</span>';
                }
            });
            // Wrap a combobox in a <select> tag
            if (_inputType=='combobox') _display = '<select style="width:152px" ' + _disabled + '>' + _display + '</select>';
        }

        // Create and style the survey question element
            // <span><img src="' + Website.global.images + 'greenCheck.png"/></span>
        var _question = '<li id="' + _questionId + ((jsonIn.cssClass)?'" class="'+jsonIn.cssClass:'') ;
        if (_answered) _question += (this.successType && this.successType.toLowerCase() == "valid")?' surveyAnswerGood':(_currAnswer)?' surveyAnswerError':'';
        _question += '">' + ((this.preLabel && this.preLabel!='')?'<label for="' + _questionId + '"> ' + this.preLabel + '</label>':'' ) + '<br/>' + _display +
                    '<img class="surveyQuestionCancel" src="images/surveyUndoQuestion.png">' +  // Question cancel button
                '</li>' + "\n";

        // Now place place the question where it needs to go
        switch(_action) {
        case 'update':
            $('#'+_questionId).replaceWith(_question);
            break;
        case 'disable':
            $('#'+_questionId).replaceWith(_question);
            $('#'+_questionId).children().attr('disabled','disabled');
            break;
        case 'delete':
            $('#'+_questionId).remove();
            return;
        default: // 'add'
            if (this.position==null) {
                $('#'+_surveyId).append(_question);
            } else {
                $('.'+jsonIn.cssClass).eq(this.position).before(_question);
            }
        }
    });

    // Delegate the appropriate events and assing the publish to the cssClass passed in.  Look for subscribers with /suite/surveyAnswer/[cssClass]
    if (!jsonIn.updateQuestions) {  // Events have already been delegated for updateQuestions
        $('#'+_surveyId).delegate('button', 'click', function(e){ $.publish('/suite/surveyAnswer/' + jsonIn.cssClass, [e, _surveyId]); return false });
        $('#'+_surveyId).delegate(':not(:button)', 'change', function(e){ $.publish('/suite/surveyAnswer/' + jsonIn.cssClass, [e, _surveyId]); return false });
    }
}

String.prototype.toCamel = function() {
    // Converts a string with spaces to camelCase
    return this.replace(/(\s[a-z|A-Z])/g, function($1){return $1.toUpperCase().replace(' ','');});
};
String.prototype.wrapTag = function(tag, condition) {
    // Wraps a tag around a string based on a condition.
    return (condition)?('<' + tag + '>' + this + '</' + tag + '>'):this;
};
function factDateFormatter (cellvalue, options, rowObject) {
    if (!cellvalue || cellvalue.length < 10) return '';

    // All fact dates come in the format "1997-03-05T00:00:00-08:00"
    // Formatting function for date fields from the fact services
    var _month = 'JanFebMarAprMayJunJulAugSepOctNovDec'.substr(parseInt(cellvalue.substr(5,2))*3-3, 3);
    var _newDate = cellvalue.substr(8,2)+'-'+_month+'-'+cellvalue.substr(0,4)+' ' +cellvalue.substr(21,5);
    return '<span' + ((rowObject.title)?' title="' + rowObject.title + '"':'') + '>' + _newDate + '</span>';
}

var sort_by = function(field, reverse, primer) {
    // Sort comparator to pass to the sort() function
    // Convert the "field" passed in using an optional "primer" function
    reverse = (reverse) ? -1 : 1;

    return function(a,b){

       a = a[field];
       b = b[field];

       if (typeof(primer) != 'undefined'){
           a = primer(a);
           b = primer(b);
       }

       if (a<b) return reverse * -1;
       if (a>b) return reverse; // * 1;
       return 0;

    }
};
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}
function concatObject(obj) {
  str='';
  for(prop in obj)
  {
    str+=prop + " value :"+ obj[prop]+"\n";
  }
  return(str);
}
function getObjValues(objData) {
    // Create a new object of just the single data values, no arrays
    var _newData = {};
    for (prop in objData) {
        if (objData.hasOwnProperty(prop) && !$.isArray(objData[prop])) _newData[prop]=objData[prop];
    }
    return _newData;
}

// Portal template resizing class
var resizeContent = {
    windowH:0,
    suiteHeaderH:null,
    suiteBody:$('#suiteBody'),
    suiteBodyHeaderH:0,
    suiteSplitTop:null,
    suiteSplitTopHeaderH:0,
    suiteSplitTopH:0,
    suiteBodyHeaderBottomH:0,
    suiteSplitBottom:null,
    suiteSplitBottomH:0,
    suiteBodyH:0,
    suiteFooterH:0,  // Can be set initially because the height will never change
    gridT:null,     // Top grid
    gridB:null,     // Bottom grid
    gridTHH:0,      // Top grid header height
    gridBHH:0,      // Bottom grid header height

    init: function() {
        this.suiteBody = $('#suiteBody');
        this.suiteSplitTop=$('#suiteSplitTop');
        this.suiteSplitBottom=$('#suiteSplitBottom');
        this.gridT = $('#suiteTopGrid');
        this.gridB = $('#suiteBottomGrid');
        this.suiteFooterH = $('#suiteFooter').height();

        this.suiteHeaderH=null;  // forces refresh
        this.updateHeight();
    },
    updateAll: function() {
        this.updateHeight();
        this.updateWidth();
    },
    updateHeight: function() {
        this.windowH = $(window).height();
        this.gridTHH = ($('#gview_suiteTopGrid .ui-jqgrid-hdiv').is(":visible")) ? $('#gview_suiteTopGrid .ui-jqgrid-hdiv').outerHeight() : 0;
        this.gridBHH = ($('#gview_suiteBottomGrid .ui-jqgrid-hdiv').is(":visible")) ? $('#gview_suiteBottomGrid .ui-jqgrid-hdiv').outerHeight() : 0;
//$.publish( '/suite/debug', [ new Date() +  ': suiteutil :' + $('#gview_suiteBottomGrid .ui-jqgrid-hdiv').height() ] );
        if (!this.suiteHeaderH != $('#suiteHeader').height()) {
            this.suiteHeaderH = $('#suiteHeader').height();
            this.suiteBodyHeaderH = $('#suiteBodyHeader').height();
            this.suiteSplitTopHeaderH = $('#suiteSplitTopHeader').height();
            this.suiteBodyHeaderBottomH=$('#suiteBodyHeaderBottom').height();
        }
        this.suiteSplitTopH = this.suiteSplitTop.height();
        this.suiteBodyH = this.windowH - this.suiteHeaderH - this.suiteFooterH - this.suiteBodyHeaderH - 2; // Subtract the bottom padding and the borders

        this.suiteSplitBottomH = this.suiteBodyH - this.suiteSplitTopH - 15;
        this.suiteSplitBottom.height(this.suiteSplitBottomH);

        if (this.gridT && this.gridT.length>0) {
            this.gridT.jqGrid('setGridHeight', this.suiteSplitTopH - this.suiteSplitTopHeaderH - this.gridTHH );
        }
        if (this.gridB && this.gridB.length>0) {
            this.gridB.jqGrid('setGridHeight', this.suiteSplitBottomH - this.suiteBodyHeaderBottomH - this.gridBHH);
        }
        $.publish('/suite/contentResized',[this]);
    },
    updateGrids: function() {
        this.init();
        this.updateAll();
    },
    updateWidth: function() {
//        $.publish( '/suite/debug', [ 'suiteutil: '+ $('#gbox_suiteTopGrid').parent().width() ] )
        if (this.gridT && this.gridT.length>0) this.gridT.jqGrid('setGridWidth', this.suiteBody.width());
        if (this.gridB && this.gridB.length>0) this.gridB.jqGrid('setGridWidth', $('#gbox_suiteBottomGrid').parent().width() );
        $.publish('/suite/contentResized',[this]);
    }
};
var gridFacts = {
    colNames:[], colModel:[],
    gridCurrent:'',
    gridsPaged:{},                      // Booleans for which grids have paging
    gridsIdx:{},                        // Paging index for each grid type
    gridsLoaded:{},                     // Booleans for which grids have been loaded
    gridCache:{},                       // Entire data set for each grid type
    gridData:{},                        // Miscellaneous metadata for each grid type

    initGrid: function(grid) {
        this.gridCurrent = grid, this.gridData[grid]={};
        this.gridCache[grid]={}, this.gridsLoaded[grid]=false, this.gridsIdx[grid]=0, this.gridsPaged[grid]=false;
    },
    defineAll: function(objData) {
        // This function is only called when the patientData widget is loaded for the first time.
        this.defineListTabs(objData.listTabs);
        this.defineDetailTabs(objData.detailTabs);
        this.drawGrid($('#suiteTopGrid'), objData);
    },
    defineListTabs: function(objListTabs) {
        // List tabs are for the "upper" grid only
        this.initGrid('upper');
        if (objListTabs) sirona.getView('simpleTabs', { container:$('#factListTabs'), data:objListTabs });
    },
    defineDetailTabs: function(objDetailTabs) {
        // Detail tabs are for the "lower" grid only
        this.initGrid('lower');
        if(objDetailTabs) {
            $.extend(this.gridData['lower'], objDetailTabs[0]);
            sirona.getView('simpleTabsBottom', { container:$('#factDetailTabs'),  data:objDetailTabs });
        }

    },
    pageGrid: function(pageOffset) {
        // Paging is necessary for a grid.  Reset the display index and store the data object
        // When this is called from a widget, the pageOffset is sent for calculation of which "page" needs to show
        var _grid = this.gridCurrent, _facts = {};
        alert('suiteutil: '+ _grid);
        if (this.gridCache[_grid].gridHeaders) _facts.gridHeaders = this.gridCache[_grid].gridHeaders.slice(pageOffset, this.gridData[_grid].maxColumns );
        _facts.facts = this.gridCache[_grid].facts.slice(pageOffset, this.gridData[_grid].maxColumns );
        this.gridsPaged[_grid] = true;
        return _facts;
    },
    drawGrid: function(gridContainer, objData) {
        if (!objData.facts || objData.facts.length == 0) return false;

//        if (objData.maxColumns && (this.colModel.length > objData.maxColumns + 3) ) objData = gridFacts.pageGrid(0);

        // Define the columns
        if (objData.gridHeaders) {
            // TODO put in dynamic hidden columns
            this.colNames = ['','',''], this.colModel = [{name:'code', hidden:true},{name:'codeSystemCode', hidden:true},{name:'itemId', hidden:true}];
             $.each(objData.gridHeaders, function() {
                 gridFacts.colNames.push((this.value)?this.value:'');        // Create the headings
                 var _colModel =  { name:this.columnId, width:parseInt(this.width) }; // , align:'center'
                 if (this.formatter) {
                     _colModel.formatter = eval(this.formatter);
                 }
                 gridFacts.colModel.push(_colModel);
             });
        }

        // Draw the grid
        gridContainer.jqGrid({
            datatype: "jsonstring", datastr:objData, loadonce: true, jsonReader: { root: "facts", repeatitems:false },
            colNames:gridFacts.colNames, colModel:gridFacts.colModel,
            afterInsertRow: function(rowid, rowdata, rowelem) {
                if (rowelem.hoverTexts) gridFacts.rowHoverTexts(gridContainer, rowid, rowelem.hoverTexts);
            },
            forceFit:true, hoverrows:false, scrollrows:true, title:false,
            loadComplete: function() {
                // Option to not display the grid headings
                if (objData.visibleGridHeaders == false) {
                    gridContainer.closest('.ui-jqgrid-view').find('.ui-jqgrid-hdiv').hide();
                }
                resizeContent.updateGrids();

                // Initialize the grid after it is loaded
                gridFacts.gridsLoaded[gridFacts.gridCurrent] = true;

                if (gridContainer.getGridParam("reccount") > 0 ) {
                    gridContainer.jqGrid('setSelection',"1"); // Select the first row after the grid is loaded
                }
            },
            onSelectRow: function(rowid, status) {
                // Only registers with the user clicks, not when initially loaded
                if (objData.trxnType=='list') {
                     $.publish('/suite/gridRowClicked',[ gridContainer.getRowData(rowid)]);
                 }
            }
        });
    },
    rowHoverTexts: function(grid, rowId, aryHoversText) {
        $.each(aryHoversText, function(idx){
            if (this != '') grid.setCell(rowId, idx + 3, '', '', { title:this } );  // Add 3 to skip over the 3 hidden columns
        });
    }
};
// FORMATTERS - the gridHeaders object in the Facts could supply a "formatter" field.
//      There needs to be a formatter function created here for each possible formatter coming from PS
function pageIcon (cellvalue, options, rowObject) {
    // Formatting function for a column that displays a page icon instead of text.
    // When the user hovers over the image, publish the click so that the subscription will display it.  Passes the mouse event so it can get the coordinates
    return (cellvalue && cellvalue!='') ? '<img style="cursor:pointer" src="images/pageIcon.png" onclick="$.publish(\'/patientData/pageIconHover\',[\'' + cellvalue + '\',event])"/>':'';
}
function boldFormatter (cellvalue, options, rowObject) {
    // Formatting function for a column that displays a page icon instead of text.
    // When the user hovers over the image, publish the click so that the subscription will display it.  Passes the mouse event so it can get the coordinates
    return (cellvalue && cellvalue!='') ? '<span style="font-weight:bold">' + cellvalue + '</span>':'';
}
function factFlagFormatter (cellvalue, options, rowObject) {
    // Formatting function for boolean type fields from fact services.  Will be formatted into a checkmark or a link for a popup
    if (!cellvalue) return '';
    return (cellvalue==true)?('<img src="images/checkmark2.png"' + ((rowObject.title)?' title="' + rowObject.title + '"':'') + '>'):
       '<a href="#" onclick="$.publish(\'/patientData/pageIconHover\',[\'\',event])">Available</a>';
}

// Global subscriptions/bindings no matter which template is loaded
$.subscribe('/suite/selectDropdown', 'suite', function(objData, dropdownElement) {
    var _list = $('#suiteDropdownList');
    _list.empty().css({ "margin-top":dropdownElement.height() + "px" });        // Clear and position the dropdown
    $.each(objData.items, function() {                                          // Loop through the labels passed in and create the <li> items of the dropdown
        this.container = objData.container;                                     // Metadata to store inside the new <li>
        _list.append('<li class="suiteDropdownListItem' + ((this.cssClass)?' ' + this.cssClass:'') + '">' + this.label + '</li>');
        _list.children().last().data(this);
    });
    if (objData.filterCSS) $('.' + objData.filterCSS, _list).remove();          // Remove the filter class if provided
    var _dropdownPos = dropdownElement.offset().left + dropdownElement.width() - $('#suiteDropdown').width();   // Default aligned to the right and below the clicked element
    $('#suiteDropdown').css({"top":dropdownElement.offset().top, "left":_dropdownPos}).show();                  // Position and show the dropdown
});
$('#suiteDropdown').mouseleave(function(){$(this).hide()});                                                     // Hide the dropdown if the mouse leaves the <div>
$('#suiteDropdownList').delegate('li', 'click', function() {
    $('#suiteDropdown').hide();
    $.publish( '/suite/selectedDropdown',[ $(this).data() ] );
    return false;
});
// Subscribe to the dialog publication to pop up a dialog box
$.subscribe('/suite/dialogShow', 'suite', function(viewName, sizingObj) {
    // This subscription is passed a sizing object which has the following parameters:
    // width, height, top, left, resizable
    var _dialog = $('#suiteDialogContent');
    if (viewName == null) {
        _dialog.html(sizingObj.body || '');
        showDialog(_dialog, sizingObj);
    } else
        sirona.getView(viewName, { container:_dialog, data:sizingObj.data,
            success: function() {
                showDialog(_dialog, sizingObj);
            }
        });
});
function showDialog(dialogElement, sizingObj) {
    dialogElement.css({ width:sizingObj.width, height:sizingObj.height, left:sizingObj.left, top:sizingObj.top, resizable:true }).draggable( { containment:'#suiteDialogOverlay'} );
    // Unhide the hidden dialog after the div is loaded
    if (sizingObj.resizable) dialogElement.resizable( {  minWidth:sizingObj.width, minHeight:sizingObj.height, handles:'s',
        resize: function(e, ui) { $.publish('/suite/dialogResize',[ui]) },
        stop: function(e, ui) { $.publish('/suite/dialogResize',[ui]) }
    });  // { handles:'n,s,e,w'}
    $('.suiteDialog').show();
}
function abortTimer(viewName) {
    clearInterval(sirona.timers[viewName]);
    delete sirona.timers[viewName];
}
$.subscribe('/suite/dialogClose', 'suite', function(dialog) {
    $.unsubscribeView(dialog);
    $('.suiteDialog').hide();
    $('#suiteDialogContent').empty().removeData().resizable("destroy");  // .css({ display:'block'});
});
// If the logout topic subscription fires, then redirect to the login page
$.subscribe('/suite/logout', 'suite', function() {
    $.publish( '/suite/unloadView',[] );  // Publish when the body will be replaced, signalling widgets to do something before
    window.location = './';
});
$.subscribe('/suite/changeScreenSize', 'suite', function(element) {
    // Full screen capability subscription.  The element that initiated the request is passed here.
    // Both patient and provider templates call this function.
    var _wasFull = (element.hasClass('fullScreen'));
    $('#suiteHeaderWrapper').animate({ height:((_wasFull)?'90px':'25px') }, { easing:'linear',  animate:'slow',
         step:function(now, fx) { resizeContent.updateAll() },
         complete:function(now, fx) { resizeContent.init() }
     });
    $('#suiteContent').parent().animate({ paddingLeft:((_wasFull)?'150px':'20px') }, { easing:'linear', animate:'slow',
        step:function(now, fx) { resizeContent.updateAll() },
        complete:function(now, fx) { resizeContent.init() }
     });
    $('.normalScreen').toggle(_wasFull); $('.fullScreen').toggle(!_wasFull);
});

// Publish when the browser window changes sizes also
$(window).resize ( function() { resizeContent.updateAll() });

// Extend JQuery to add an outerHTML function
$.fn.outerHTML = function(s) {
    return (s)? this.before(s).remove(): $("<div>").append(this.eq(0).clone()).html();
};
// Extend JQuery to add a function that creates an object out of form parameters
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

var formTemplate = {
    // This class is used to populate a template of fields from a JSON structure.  The fields can be input or just labels.
    breakField:null, breakSection:null, objDataCnt:0,
    init: function() {
        this.breakField = null, this.breakSection = null, this.objDataCnt = 0;
    },
    loadForm: function(objData, template) {
        this.init();
        this.loadFields(objData, template);
    },
    loadFields: function(objData, template, prefix) {
        // This function recieves an object of key/value data fields and loads the form with the data
        if (!template) template = {};

        if (template && template.breakClass) {
            formTemplate.objDataCnt++;
            var _outputRow = $('.' + prefix + '_outputrow'); // Class of the detail output row from the templ
            
            // Check for any break fields and load the section that needs to be cloned when the break happens
            if (formTemplate.breakField != objData[template.breakClass.field]) {
                // Append a cloned {prefix}_break class if one exists.  These rows are for the header of a break.
                _outputRow.last().after(formTemplate.cloneElements($('.' + prefix + '_break')).removeClass(prefix + '_break').addClass(prefix + '_outputrow').show() );
                formTemplate.breakField = objData[template.breakClass.field];
            }
            // Append a new detail output row that needs to be cloned from the template, and then populated below
            $('.' + prefix + '_outputrow').last().after(formTemplate.cloneElements(_outputRow.first()).show() );
        }

//        if (prefix=='configuration') alert('suiteutil: '+  key1);
        $.each(objData, function(key1, val1) {
            if ($.isArray(val1)) {
                 if (typeof(val1[0])=='object') {
                    // Arrays of objects
                    formTemplate.init();
                    $.each(val1, function() { formTemplate.loadFields(this, template[key1], key1) });
                 }
            } else if (typeof(val1) == 'object') {
                // Object below root
                formTemplate.loadFields(val1, template[key1], key1)
            } else {
                // Populate the field, prepending the parent key and appending the unique count
                var _field = ((prefix)?prefix+'_':'') + key1 + ((formTemplate.objDataCnt > 0)?formTemplate.objDataCnt:'');
                var _ele = $('#'+ _field);
                if (_ele.length > 0) {          // Make sure the element exists in the form
                    var _tag = _ele.attr('tagName').toLowerCase();
                    if ('input|textarea'.indexOf(_tag) >= 0) {
                        if (template[key1] && template[key1].type && template[key1].type == 'checkbox' & val1) _ele.attr('checked','checked');
                        else _ele.val(val1);

                        if (template[key1] && template[key1].dataType == 'integer') _ele.addClass('suiteIntegerInput');
                    }
                    else if (_tag=='select') {
                        if (objData[key1+'s'])
                            $.each(objData[key1+'s'], function() { _ele.append('<option' + ((this==objData[key1])?' selected':'') + '>'  + this + '</option>')});
                    } else _ele.html(val1);
                    // Set the width of the element if provided
                    if (template[key1] && template[key1].width) _ele.width(template[key1].width);
                }
            }
        });
    },
    cloneElements: function(element) {
        // Clones all of the elements inside the passed element, and change the ids
        var _newBreak = element.clone(false);       // Clone the entire child, then replace all of the ids
        $.each(_newBreak.find("[name]"), function() { $(this).attr('name', $(this).attr('name') + '[' + formTemplate.objDataCnt + ']') });
        $.each(_newBreak.find("[id]"), function() { $(this).attr('id', $(this).attr('id') + formTemplate.objDataCnt) });
        $.each(_newBreak.find("[for]"), function() { $(this).attr('for', $(this).attr('for') + formTemplate.objDataCnt) });
        return _newBreak;
    }
};
$('body').delegate('input.suiteIntegerInput', 'keypress', function(e) {
    var key = e.which || e.keyCode || 0;
//    if (sirona.debugIt) $.publish( '/suite/debug', [ 'suiteutil: '+ e.keyCode + '/'+ key + '/' + e.shiftKey ] );
    return (!e.shiftKey && (key==8 || key==9 || key==46 || (key >= 48 && key <= 57) || (key >=34 && key <=40)) );  //  || (key >= 96 && key <= 105));
});
