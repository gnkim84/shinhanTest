<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib	prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib	prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib	prefix="spring" uri="http://www.springframework.org/tags"%>
<link href="/css/user_common.css" rel="stylesheet" type="text/css" />
<link href="/css/sub_common.css" rel="stylesheet" type="text/css" />
<link href="/css/main.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="/js/tableau-2.6.0.js"></script>
<script type="text/javascript">

	function dbDialog(kind){
        switch (kind) {
            case "workbook" :
                viz.showDownloadWorkbookDialog();
                break;
            case "pdf" :
                viz.showExportPDFDialog();
                break;
            case "img" :
                viz.showExportImageDialog();
                break;
            case "ppt" :
                //viz.showExportImageDialog();
                break;
            case "share" :
                viz.showShareDialog();
                break; 
            default :
                break;       
        }
    }

    function refresh(){
        viz.refreshDataAsync();
    }

    function reLoad(){
        viz.revertAllAsync();
    }

    function setSize(){
        viz.setFrameSize(900,500);
    }

    function redo(){
        viz.redoAsync();
    }

    function undo(){
        viz.undoAsync();
    }

    function show(){
        viz.show();
    }

    function hide(){
        viz.hide();
    }

    function copy(){
        //$("#tableau2").append($("#tableau").children().clone());
    }

    function getSize(){
        var tmp = viz.getVizSize();
        console.log(tmp);
        $("#vizContainer").css("width", tmp.sheetSize.minSize.width + 50 + "px");
        $("#vizContainer").css("height", tmp.sheetSize.minSize.height + 50 + "px");
        if(tmp.sheetSize.minSize.width > 1400){
            //$("").addClass("width100");
        }else{
            //$("").css("width", tmp.sheetSize.minSize.width + 50 + "px");
        }
        //$("").css("width", tmp.sheetSize.minSize.width + 50 + "px");
    }

    function test(){
        var wb = viz.getwWorkbook();
        var sht = wb.getActiveSheet();
        wb.activateSheetAsync(sht.getIndex());
    }

    function fullSize(){
        var o = document.getElementById("tableau");
        o.msRequestFullScreen();        
    }

    function ticketCheck(){
        var ticket = "${tableauToken}";
        var authYn = "${authYn}";

        if(authYn == "N"){
			alert("보고서에 접근 권한이 없습니다.");
			return false;
		}
        if(ticket == "-1"){
            alert("토큰 발행 중 에러가 발생했습니다.");
            return false;
        }else if(ticket == "null"){
        	alert("토큰 발행 중 에러가 발생했습니다.");
            return false;
        }else{
        	initViz();
        }
    }

    function initViz(){
        var fullUrl = "${reportViewUrl}";
        var containerDiv = document.getElementById("vizContainer"),
            url = fullUrl,
            options = {
                hideTabs : false,
                //hideToolbar : true,
                onFirstVizSizeKnown : function(){
                    //getSize();
                },
                onFirstInteractive : function (){
                    console.log("Run this code when the viz has finished loading");
                }
            };
        var viz = new tableau.Viz(containerDiv, fullUrl,options);
    }
</script>
<body onload="javascript:ticketCheck();">
	<div id="header">
	
	</div>
	<div id="vizContainer" style="width:100%; height:100%; text-align:center;"></div>
</body>