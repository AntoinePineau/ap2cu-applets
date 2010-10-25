function FlagImage(width,height,x,y) {
  this.width = width;
  this.height = height;
  this.x = x;
  this.y = y;
  this.toString = function() {
    return "<img src='/res/img/none.gif' width='"+width+"' height='"+height+"' style='background-image:url(/tools/flags/flags.png); background-repeat:no-repeat; background-position:-"+x+"px -"+y+"px;'/>";
  };
}
function getImageToWrite(size,country) {
  if(country == "" || country == null) country="__";
  if(size == "" || size == null) size="normal";
  size = size.toLowerCase();
  country = country.toUpperCase();
  var width=0,height=0,x=0,y=0,start=0;
  if(size=="small") { width=16; height=12; start=6720; }
  else if(size=="big") { width=46; height=33; y=22; }
  else if(size) { width=30; height=22; }
  var codes = "__,AD,AE,AF,AG,AI,AL,AM,AN,AO,AQ,AR,AS,AT,AU,AW,AZ,BA,BB,BD,BE,BF,BG,BH,BI,BJ,BM,BN,BO,BR,BS,BT,BW,BY,BZ,CA,CD,CF,CG,CH,CI,CK,CL,CM,CN,CO,CR,CU,CV,CY,CZ,DE,DJ,DK,DM,DO,DZ,EC,EE,EG,EH,ER,ES,ET,FI,FJ,FM,FO,FR,GA,GB,GD,GE,GG,GH,GI,GL,GM,GN,GP,GQ,GR,GT,GU,GW,GY,HK,HN,HR,HT,HU,ID,IE,IL,IM,IN,IQ,IR,IS,IT,JE,JM,JO,JP,KE,KG,KH,KI,KM,KN,KP,KR,KW,KY,KZ,LA,LB,LC,LI,LK,LR,LS,LT,LU,LV,LY,MA,MC,MD,ME,MG,MH,MK,ML,M,MN,MO,MQ,MR,MS,MT,MU,MV,MW,MX,MY,MZ,NA,NC,NE,NG,NI,NL,NO,NP,NR,NZ,OM,PA,PE,PF,PG,PH,PK,PL,PR,PS,PT,PW,PY,QA,RE,RO,RS,RU,RW,SA,SB,SC,SD,SE,SG,SI,SK,SL,SM,SN,SO,SR,ST,SV,SY,SZ,TC,TD,TG,TH,TJ,TL,TM,TN,TO,TR,T,TV,TW,TZ,UA,UG,US,UY,UZ,VA,VC,VE,VG,VI,VN,VU,WS,YE,ZA,ZM,ZW".split(",");
  for(var codeIndex=0,x=start;codeIndex<codes.length;codeIndex++,x+=width) {
    if(codes[codeIndex]==country) break;
  }
  return new FlagImage(width, height, x, y);
}