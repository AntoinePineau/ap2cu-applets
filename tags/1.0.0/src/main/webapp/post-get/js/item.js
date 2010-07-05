function collapseAll(collapse) {
  $('img[title='+(collapse?'Collapse':'Expand')+']').click();
  $('#collapse').text(!collapse?'Collapse all':'Expand all');
  $('#collapse').unbind();
  $('#collapse').click(function(){collapseAll(!collapse)});
}
function collapse(id,btn) {
  var $id=$('#'+id);
  if($id.css('display')=='none'){$id.show();btn.src='http://ap2cu.com/tools/test/img/icn_collapse.gif';}
  else{$id.hide();btn.src='http://ap2cu.com/tools/test/img/icn_expand.gif';}
}
var regexCONTAINER=new RegExp('^((.*?)container-)(\\d+)$');
function addItem(id) {
  var $id=$(document.getElementById(id));
  var container=regexCONTAINER.exec(id);
  
  var containerName=container[1];
  var typeOfContainer=container[2];
  var lastIdValue=container[3];
      
  if(container && container.length>2) {
	  var nb=0;
	  $('tr').each(function(){
	    if($(this).attr('id').indexOf(containerName)==0) {
	      var i=parseInt(this.id.replace(regexCONTAINER,'$3'));
	      if(nb<i)nb=i;
	    }
	  });
    var regexID=new RegExp('-'+lastIdValue,'g');
	  $id.parent().append(load(typeOfContainer, nb+1));
  }
}
function removeItem(id) {
  var $id=$('#'+id);
  $id.remove();
}
function moveItem(id,up){
  var $curr=$('#'+id);
  var $prev=up?$curr.prev():$curr.next();
  var tmpHtml=$curr.html();
  $curr.html($prev.html().replace(new RegExp('','g')));
  $prev.html(tmpHtml);
  var tmpId=$curr.attr('id');
  $curr.attr('id',$prev.attr('id'));
  $prev.attr('id',tmpId);
}
function moveItemUp(id) {
  moveItem(id,true);
}
function moveItemDown(id) {
  moveItem(id,false);
}

var regexINSERT=new RegExp('<!--@INSERT:([^(]+?)(?:\\\(([^)]*?)\\\))?@-->');
function load(type,n) {
  var content='';
  $.ajax({url:'containers/'+type+'.xml',dataType:'text',async:false,success:function(data){content=data.replace(/>\s*</g,'><');},error:function(XMLHttpRequest,textStatus,errorThrown){alert(textStatus);}});
  var insertions='';
  content=content.replace(new RegExp('<!--@REPLACE:'+type+'-id@-->','g'),n).replace(new RegExp('<span class="nb"> - \\d+</span>'), '<span class="nb"> - '+n+'</span>');
  while(true) {
    insertions=regexINSERT.exec(content);
    if(insertions && insertions[1]!='') {
      var text=load(insertions[1],'1');
      if(insertions[2]&&insertions[2]!='') {
        var params=insertions[2].split(',');
        for(i=0;i<params.length;i++) {
          var param=params[i].split('=');
          text=text.replace(new RegExp('<!--@REPLACE:'+param[0]+'@-->','g'),param[1]);
        }
      }
      content=content.replace(regexINSERT,text);
    }
    else break;
  }
  return content;     
}
function update() {
  $("img[title=Remove][onclick$=\"-1')\"]").each(function(){
    $(this).attr('src','http://ap2cu.com/tools/test/img/btn_repliremove_gray.gif');
    $(this).removeAttr('title');
    $(this).removeAttr('onclick');
  });
}