/*!
######################################################

# ORACLELIB.JS

# OCOM GLOBAL ASSET RELEASE: v7.7.0

# BUILD DATE: MON JAN 26 18:56:00 UTC 2015

# COPYRIGHT ORACLE CORP 2015 [UNLESS STATED OTHERWISE]

# ANY CHANGES MADE TO THIS FILE WILL BE OVERWRITTEN!
# DO NOT MODIFY THIS FILE ON STAGE OR PRODUCTION. ALL
# CHANGES OR ADDITIONS TO THIS FILE MUST BE SUBMITTED
# TO WEBSTANDARDS_WW -AT- ORACLE.COM

######################################################
*/

/*! ORACLELIB.PROFILE.LEGACY  */
function printWelcome(){var d=window.location.href;var c=d;var a=c.split("/");var b='<span class="profile">';if(USER.guid){if(a[4]=="global"){b+="Welcome "+USER.firstname+' ( <a class="profile" href="javascript:sso_sign_out();">Sign Out</a> | <a class="profile" href="https://profile.oracle.com/EndUser/faces/info/internalAccount.jspx?nextURL=http://www.oracle.com/us/index.html?'+a[5]+'">Account</a> )'}else{b+="Welcome "+USER.firstname+' ( <a class="profile" href="javascript:sso_sign_out();">Sign Out</a> | <a class="profile" href="https://profile.oracle.com/EndUser/faces/info/internalAccount.jspx?nextURL=http://www.oracle.com/us/index.html">Account</a> )'}}else{if(a[4]=="global"){b+='<a href="http://www.oracle.com/webapps/redirect/signon?nexturl=http://www.oracle.com/us/index.html?'+a[5]+'">(Sign In / Register for a free Oracle Web account)</a>'}else{b+='<a href="http://www.oracle.com/webapps/redirect/signon?nexturl=http://www.oracle.com/us/index.html">(Sign In / Register for a free Oracle Web account)</a>'}}b+="</span>";document.write(b);document.close()}function printWelcomeOPN(){var a=location.href;if(a.indexOf("html?loc")>-1){var c=a.replace(/^[^=]+\=/,"");loc=c}else{loc=window.location.href}loc=escape(loc);var b='<span class="profile">';if(USER.guid){b+="Welcome "+USER.firstname+' ( <a class="profile" href="javascript:sso_sign_out();">Sign Out</a> | <a class="profile" href="http://www.oracle.com/partners/admin/web_account.html?loc='+loc+'">Account</a> )'}else{b+='<a href="http://www.oracle.com/partners/admin/web_account.html?loc='+loc+'">(Sign In / Register for a free Oracle Web account)</a>'}b+="</span>";document.write(b);document.close()}function printWelcomeSES(b){var a='<span class="profile">';if(b!=null&&b!=""){a+="Welcome "+USER.firstname+' ( <a class="profile" href="/search/logout.jsp?return_url=/search/search">Sign Out</a> | <a class="profile" href="http://www.oracle.com/admin/account/index.html">Account</a> )'}else{a+='<a href="/search/logout.jsp?return_url=/search/search">(Sign In)</a>'}a+="</span>";document.write(a);document.close()}

/*! ORACLELIB.PROFILE  */
var USER=new getUserInfo();var language_root="";function existsUCMCookie(a){if(a=="ORA_UCM_INFO"){if((ORA_UCM_INFO.version!=null)&&(ORA_UCM_INFO.guid!=null)&&(ORA_UCM_INFO.username!=null)){return true}}return false}function isUCMRegistered(){if(existsUCMCookie("ORA_UCM_INFO")==true){orainfo_exists=true;otnnm_exists=true;return true}return false}function getArg(f,d){var c="",b="";if(!d){d=location.search.substring(1)}if(!d){return c}else{var e=d.split("&");for(i=0;i<e.length;i++){b=e[i].toUpperCase();if(b.indexOf(f.toUpperCase()+"=")!=-1){var a=e[i].split("=");c=a[1]}}}return c}function isUCMAnonymous(){if((ORA_UCM_INFO.version!=null)&&(ORA_UCM_INFO.guid!=null)&&(isUCMRegistered()==false)){return true}else{return false}}function getUCMCookies(){ORA_UCM_INFO=new private_ORA_UCM_INFO()}function signout(a){rUrl=encodeURIComponent(window.location.href);if(window.location.host.indexOf("-stage")>-1){window.location="http://login-stage.oracle.com/sso/logout?p_done_url="+rUrl}else{window.location="http://login.oracle.com/sso/logout?p_done_url="+rUrl}}function getCookieData(c){var g=c.length;var e=document.cookie.length;var d=0;var f;while(d<e){var b=d+g;if(document.cookie.substring(d,b)==c){f=document.cookie.indexOf(";",b);if(f==-1){f=document.cookie.length}b++;var a=decodeURIComponent(document.cookie.substring(b,f).replace(/\+/g,"%20"));return cleanCookieContent(a)}d++}return""}function cleanCookieContent(b){var d=(typeof(b)=="undefined")?"NoData":b;var a="<>";if(d!="NoData"){var c=d.length;for(i=0;i<c;i++){if(d.substr(i,1)!="."&&d.substr(i,1)!="?"&&a.search(d.substr(i,1))>-1){d="Invalid";i=c+1}}}return d}function getUserInfo(){var a=new Object();this.value_enc=getCookieData("ORA_UCM_INFO");this.array=this.value_enc.split("~");a.version=this.array[0];a.guid=this.array[1];a.firstname=this.array[2];a.lastname=this.array[3];a.username=this.array[4];return a}function invalidateAuthCookie(){var b=getCookieData("ORASSO_AUTH_HINT");if(b!=null){var a="ORASSO_AUTH_HINT=INVALID; Max-Age=0; domain=.oracle.com; path=/;";document.cookie=a}}function sso_sign_out(){rUrl=escape(window.location.href);if((rUrl.indexOf("/secure")!=-1)){rUrl="http://www.oracle.com/partners/"}invalidateAuthCookie();if(window.location.host.indexOf("-stage")>-1){window.location="https://login-stage.oracle.com/sso/logout?p_done_url="+rUrl}else{window.location="https://login.oracle.com/sso/logout?p_done_url="+rUrl}}function private_UCMCookieDecode(d){var e=" !\"#$&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[]^_`abcdefghijklmnopqrstuvwxyz{|}~.";var a=unescape(d);var c="";var b="";for(i=0;i<a.length;i++){b=a.charAt(i);j=e.indexOf(b);if(j!=-1){j+=2;if(j>(e.length-1)){j-=e.length}c+=e.charAt(j)}else{c+=b}}return c}function private_ORA_UCM_INFO(){this.value_enc=getCookieData("ORA_UCM_INFO");this.array=this.value_enc.split("~");this.version=this.array[0];this.guid=this.array[1];this.firstname=this.array[2];this.lastname=this.array[3];this.username=this.array[4];var b=["3",this.guid,this.firstname,this.lastname,this.username];var a=b.join("~")}var min=(60*1000);var hour=(60*min);var day=(24*hour);var year=(365*day);

/*! ORACLELIB.GLOBALMENU  */
function worldwideCountries(g){if(g=="hp"){document.writeln("<div id='panelDiv' style=\"position:absolute; visibility:hidden; left:0px; z-index:20000; WIDTH: 620px; BORDER-RIGHT: #9A9A9A 1px solid; PADDING-RIGHT: 12px;BORDER-TOP: #9A9A9A 1px solid; PADDING-LEFT: 12px; PADDING-BOTTOM: 12px; BORDER-LEFT: #9A9A9A 1px solid; PADDING-TOP: 12px;BORDER-BOTTOM: #9A9A9A  1px solid; BACKGROUND-COLOR: #ffffff\" onmouseover=\"panelMOv('panelDiv','img1');\" onmouseout=\"panelMOu('panelDiv');\">")}else{if(g=="events"){document.writeln("<div id='evSearch' style=\"position:absolute; right:10px; top:23px; visibility:hidden; z-index:20000; WIDTH: 620px; BORDER-RIGHT: #9A9A9A 1px solid; PADDING-RIGHT: 12px;BORDER-TOP: #9A9A9A 1px solid; PADDING-LEFT: 12px; PADDING-BOTTOM: 12px; BORDER-LEFT: #9A9A9A 1px solid; PADDING-TOP: 12px;BORDER-BOTTOM: #9A9A9A 1px solid; BACKGROUND-COLOR: #ffffff\" onmouseover=\"panelMOv('evSearch','img1');\" onmouseout=\"panelMOu('evSearch');\">")}else{document.writeln("<div id='panelDiv' style=\"position:absolute; visibility:hidden; left:0px; z-index:20000; WIDTH: 620px; BORDER-RIGHT: #9A9A9A 1px solid; PADDING-RIGHT: 12px;BORDER-TOP: #9A9A9A 1px solid; PADDING-LEFT: 12px; PADDING-BOTTOM: 12px; BORDER-LEFT: #9A9A9A 1px solid; PADDING-TOP: 12px;BORDER-BOTTOM: #9A9A9A 1px solid; BACKGROUND-COLOR: #ffffff\" onmouseover=\"panelMOv('panelDiv','img1');\" onmouseout=\"panelMOu('panelDiv');\">")}}var e='<div nowrap><a href="http://www.oracle.com/';var d='" onClick="s_objectID=\'R8:WW:';var f="</a></div>";document.writeln('<table width="100%" class="sngPstMenu"><tr align=left><td colspan=5 class="sngPst" style="border-bottom:#cccccc 1px solid" title="select a country region"><b>SELECT A COUNTRY/REGION</b></td></tr><tr valign=top align=left><td width=20% class="sngPst">'+e+"ao/index.html"+d+'AFRICA OPERATIONS\';" id="Africa Operations">Africa Operation'+f+""+e+"lad/index.html"+d+'ARGENTINA\';" id="Argentina">Argentina'+f+""+e+"au/index.html"+d+'AUSTRALIA\';" id="Australia">Australia'+f+""+e+"at/index.html"+d+'AUSTRIA\';" id="Austria">Austria'+f+""+e+"me/index.html"+d+'BAHRAIN\';" id="Bahrain">Bahrain'+f+""+e+"as/corporate/contact/bangladesh-316183-en-as.html"+d+'BANGLADESH\';" id="Bangladesh">Bangladesh'+f+""+e+"be/index.html"+d+'BELGIUM AND LUXEMBOURG\';" id="Belgium &amp; Luxembourg">Belgium &amp; Luxembourg'+f+""+e+"lad/index.html"+d+'BELIZE\';" id="Belize">Belize'+f+""+e+"as/corporate/contact/bhutan-316187-en-as.html"+d+'BHUTAN\';" id="Bhutan">Bhutan'+f+""+e+"lad/index.html"+d+'BOLIVIA\';" id="Bolivia">Bolivia'+f+""+e+"ba/index.html"+d+'BOSNIA AND HERZEGOVINA\';" id="Bosnia &amp; Herzegovina">Bosnia &amp; Herzegovina'+f+""+e+"br/index.html"+d+'BRASIL\';" id="Brasil">Brasil'+f+""+e+"as/corporate/contact/brunei-316198-en-as.html"+d+'BRUNEI\';" id="Brunei">Brunei'+f+""+e+"bg/index.html"+d+'BULGARIA\';" id="Bulgaria">Bulgaria'+f+""+e+"as/corporate/contact/cambodia-316193-en-as.html"+d+'CAMBODIA\';" id="Cambodia">Cambodia'+f+""+e+"ca-en/index.html"+d+'CANADA - ENGLISH\';" id="Canada - English">Canada - English'+f+""+e+"ca-fr/index.html"+d+'CANADA - FRENCH\';" id="Canada - French">Canada - French'+f+""+e+"lad/index.html"+d+'CHILE\';" id="Chile">Chile'+f+""+e+"cn/index.html"+d+'CHINA\';" id="China">China'+f+""+e+"lad/index.html"+d+'COLOMBIA\';" id="Colombia">Colombia'+f+"</td><td width=20% class='sngPst'>"+e+"lad/index.html"+d+'COSTA RICA\';" id="Costa Rica">Costa Rica'+f+""+e+"hr/index.html"+d+'CROATIA\';" id="Croatia">Croatia'+f+""+e+"cy/index.html"+d+'CYPRUS\';" id="Cyprus">Cyprus'+f+""+e+"cz/index.html"+d+'CZECH REPUBLIC\';" id="Czech Republic">Czech Republic'+f+""+e+"dk/index.html"+d+'DENMARK\';" id="Denmark">Denmark'+f+""+e+"lad/index.html"+d+'ECUADOR\';" id="Ecuador">Ecuador'+f+""+e+"me/index.html"+d+'EGYPT\';" id="Egypt">Egypt'+f+""+e+"ee/index.html"+d+'ESTONIA\';" id="Estonia">Estonia'+f+""+e+"fi/index.html"+d+'FINLAND\';" id="Finland">Finland'+f+""+e+"fr/index.html"+d+'FRANCE\';" id="France">France'+f+""+e+"de/index.html"+d+'GERMANY\';" id="Germany">Germany'+f+""+e+"gr/index.html"+d+'GREECE\';" id="Greece">Greece'+f+""+e+"lad/index.html"+d+'GAUTEMALA\';" id="Guatemala">Guatemala'+f+""+e+"lad/index.html"+d+'HONDURAS\';" id="Honduras">Honduras'+f+""+e+"hk/index.html"+d+'HONGKONG\';" id="Hong Kong">Hong Kong'+f+""+e+"hu/index.html"+d+'HUNGARY\';" id="Hungary">Hungary'+f+""+e+"in/index.html"+d+'INDIA\';" id="India">India'+f+""+e+"as/index.html"+d+'INDONESIA\';" id="Indonesia">Indonesia'+f+""+e+"me/index.html"+d+'IRAQ\';" id="Iraq">Iraq'+f+""+e+"ie/index.html"+d+'IRELAND\';" id="Ireland">Ireland'+f+"</td><td width=20% class='sngPst'>"+e+"il/index.html"+d+'ISRAEL\';" id="Israel">Israel'+f+""+e+"it/index.html"+d+'ITALY\';" id="Italy">Italy'+f+'<div><a href="http://www.oracle.co.jp/'+d+'JAPAN\';" id="Japan">Japan'+f+""+e+"me/index.html"+d+'JORDAN\';" id="Jordan">Jordan'+f+""+e+"ru/index.html"+d+'KAZAKHSTAN\';" id="Kazakhstan">Kazakhstan'+f+""+e+"kr/index.html"+d+'KOREA\';" id="Korea">Korea'+f+""+e+"me/index.html"+d+'KUWAIT\';" id="Kuwait">Kuwait'+f+""+e+"as/corporate/contact/laos-316260-en-as.html"+d+'LAOS\';" id="Laos">Laos'+f+""+e+"lv/index.html"+d+'LATVIA\';" id="Lativa">Latvia'+f+""+e+"me/index.html"+d+'LEBANON\';" id="Lebanon">Lebanon'+f+""+e+"lt/index.html"+d+'LITHUANIA\';" id="Lithuania">Lithuania'+f+""+e+"as/index.html"+d+'MALAYSIA\';" id="Malaysia">Malaysia'+f+""+e+"as/corporate/contact/maldives-316209-en-as.html"+d+'MALDIVES\';" id="Maldives">Maldives'+f+""+e+"mt/index.html"+d+'MALTA\';" id="Malta">Malta'+f+""+e+"lad/index.html"+d+'MEXICO\';" id="Mexico">Mexico'+f+""+e+"ru/index.html"+d+'MOLDOVA\';" id="Moldova">Moldova'+f+""+e+"as/corporate/contact/nepal-316215-en-as.html"+d+'NEPAL\';" id="Nepal">Nepal'+f+""+e+"nl/"+d+'NETHERLANDS\';" id="Netherlands">Netherlands'+f+""+e+"nz/index.html"+d+'NEW ZEALAND\';" id="New Zealand">New Zealand'+f+""+e+"lad/index.html"+d+'NICARAGUA\';" id="Nicaragua">Nicaragua'+f+"</td><td width=20% class='sngPst'>"+e+"no/index.html"+d+'NORWAY\';" id="Norway">Norway'+f+""+e+"me/index.html"+d+'OMAN\';" id="Oman">Oman'+f+""+e+"as/corporate/contact/pakistan-316185-en-as.html"+d+'PAKISTAN\';" id="Pakistan">Pakistan'+f+""+e+"lad/index.html"+d+'PANAMA\';" id="Panama">Panama'+f+""+e+"lad/index.html"+d+'PARAGUAY\';" id="Paraguay">Paraguay'+f+""+e+"lad/index.html"+d+'PERU\';" id="Peru">Peru'+f+""+e+"as/index.html"+d+'PHILLIPINES\';" id="Philippines">Philippines'+f+""+e+"pl/"+d+'POLAND\';" id="Poland">Poland'+f+""+e+"pt/index.html"+d+'PORTUGAL\';" id="Portugal">Portugal'+f+""+e+"lad/index.html"+d+'PUERTO RICO\';" id="Puerto Rico">Puerto Rico'+f+""+e+"me/index.html"+d+'QATAR\';" id="Qatar">Qatar'+f+""+e+"ro/index.html"+d+'ROMANIA\';" id="Romania">Romania'+f+""+e+"ru/index.html"+d+'RUSSIA\';" id="Russia">Russia'+f+""+e+"me/index.html"+d+'SAUDI ARABIA\';" id="Saudi Arabia">Saudi Arabia'+f+""+e+"yu/index.html"+d+'SERBIA AND MONTENGRO\';" id="Serbia &amp; Montenegro">Serbia &amp; Montenegro'+f+""+e+"as/index.html"+d+'SINGAPORE\';" id="Singapore">Singapore'+f+""+e+"sk/index.html"+d+'SLOVAKIA\';" id="Slovakia">Slovakia'+f+""+e+"si/index.html"+d+'SLOVENIA\';" id="Slovenia">Slovenia'+f+""+e+"za/index.html"+d+'SOUTH AFRICA\';" id="South Africa">South Africa'+f+""+e+"es/index.html"+d+'SPAIN\';" id="Spain">Spain'+f+"</td><td width=20% class='sngPst'>"+e+"as/corporate/contact/srilanka-316250-en-as.html"+d+'SRI LANKA\';" id="Sri Lanka">Sri Lanka'+f+""+e+"se/index.html"+d+'SWEDEN\';" id="Sweden">Sweden'+f+""+e+"ch-fr/index.html"+d+'SWITZERLAND -- FRENCH\';" id="Switzerland - French">Switzerland -- French'+f+""+e+"ch-de/index.html"+d+'SWITZERLAND -- GERMAN\';" id="Switzerland - German">Switzerland -- German'+f+""+e+"tw/index.html"+d+'TAIWAN\';" id="Taiwan">Taiwan'+f+""+e+"as/index.html"+d+'THAILAND\';" id="Thailand">Thailand'+f+""+e+"tr/index.html"+d+'TURKEY\';" id="Turkey">Turkey'+f+""+e+"ru/index.html"+d+'UKRAINE\';" id="Ukraine">Ukraine'+f+""+e+"me/index.html"+d+'UNITED ARAB EMIRATES\';" id="United Arab Emirates">United Arab Emirates'+f+""+e+"uk/index.html"+d+'UNITED KINGDOM\';" id="United Kingdom">United Kingdom'+f+""+e+"us/index.html"+d+'UNITED STATES\';" id="United States">United States'+f+""+e+"lad/index.html"+d+'URAGUAY\';" id="Uruguay">Uruguay'+f+""+e+"lad/index.html"+d+'VENEZUELA\';"id="Venezuela">Venezuela'+f+""+e+"as/corporate/contact/vietnam-316254-en-as.html"+d+'VIETNAM\';" id="Vietnam">Vietnam'+f+""+e+"me/index.html"+d+'YEMEN\';" id="Yemen">Yemen'+f+"</td></tr><tr><td></td></tr></table></div>")}function panelMOv(){if(navigator.appName!="Netscape"){var b=document.getElementById("group");b.style.visibility="hidden"}var a=document.getElementById("panelDiv");a.style.visibility="visible"}function panelMOu(){var a=document.getElementById("panelDiv");a.style.visibility="hidden";if(navigator.appName!="Netscape"){var b=document.getElementById("group");b.style.visibility="visible"}}function mvqMOu(){var a=document.getElementById("panelDiv");a.style.visibility="hidden";if(navigator.appName!="Netscape"){var b=document.getElementById("group");b.style.visibility="visible"}}function findPosX(a){var b=0;if(a.offsetParent){while(1){b+=a.offsetLeft;if(!a.offsetParent){break}a=a.offsetParent}}else{if(a.x){b+=a.x}}return b}function findPosY(b){var a=0;if(b.offsetParent){while(1){a+=b.offsetTop;if(!b.offsetParent){break}b=b.offsetParent}}else{if(b.y){a+=b.y}}return a}function positionedOffsetLeft(b){var a=0,d=0;do{d+=b.offsetLeft||0;b=b.offsetParent;if(b){if(b.tagName.toUpperCase()=="BODY"){break}var c=Element.getStyle(b,"position");if(c!=="static"){break}}}while(b);return d}function positionedOffsetTop(b){var a=0,d=0;do{a+=b.offsetTop||0;b=b.offsetParent;if(b){if(b.tagName.toUpperCase()=="BODY"){break}var c=Element.getStyle(b,"position");if(c!=="static"){break}}}while(b);return a}function panelMOv(a){if(navigator.appName!="Netscape"){var c=document.getElementById("group")}var b=document.getElementById(a);b.style.visibility="visible"}function panelMOu(a){var b=document.getElementById(a);b.style.visibility="hidden";if(navigator.appName!="Netscape"){var c=document.getElementById("group")}}function mvqMOv(d,b){var e=document.getElementById(b);if(typeof e!="undefined"&&e!=null){var a=positionedOffsetLeft(e);var h=positionedOffsetTop(e);var c=e.width;if(d=="panelDiv"){document.getElementById(d).style.left=a-(413)+"px";document.getElementById(d).style.top=h+9+"px"}else{if(d=="panelDiv_iam"||d=="panelDiv_comm"){document.getElementById(d).style.left=a-195+"px";document.getElementById(d).style.top=h+9+"px"}else{if(d=="panelDiv_iwanto"){document.getElementById(d).style.left=a-195+"px";document.getElementById(d).style.top=h+9+"px"}else{if(d=="panelDiv_search"){if(location.pathname.indexOf("/partners")==0){document.getElementById(d).style.left=a-5+"px";document.getElementById(d).style.top=h-(navigator.appName!="Netscape"?20:7)+"px"}else{document.getElementById(d).style.left=a-5+"px";document.getElementById(d).style.top=h-(navigator.appName!="Netscape"?5:7)+"px"}}else{if(d=="panelDiva"){document.getElementById(d).style.left=a-35+"px";document.getElementById(d).style.top=h+18+"px"}else{if(d=="OPNpanelDiv"){document.getElementById(d).style.left=a-(navigator.appName!="Netscape"?340:365)+"px";document.getElementById(d).style.top=h+12+"px"}else{if(d=="panelDivOTN"){document.getElementById(d).style.left=a-195+"px";document.getElementById(d).style.top=h+9+"px"}else{if(d=="evSearch"){document.getElementById(d).style.left=a-(413)+"px";document.getElementById(d).style.top=h+9+"px"}}}}}}}}}if(navigator.appName!="Netscape"){var g=document.getElementById("group")}var f=document.getElementById(d);f.style.visibility="visible"}function mvqMOu(a){var b=document.getElementById(a);b.style.visibility="hidden";if(navigator.appName!="Netscape"){var c=document.getElementById("group")}}function OTNWorldwideCountries(a){document.writeln("<div id='panelDivOTN' style=\"position:absolute;left:0px;visibility:hidden; z-index:3700; WIDTH: 180px; BORDER-RIGHT: #9A9A9A 1px solid; PADDING-RIGHT: 12px;BORDER-TOP: #9A9A9A 1px solid; PADDING-LEFT: 12px; PADDING-BOTTOM: 12px; BORDER-LEFT: #9A9A9A 1px solid; PADDING-TOP: 12px;BORDER-BOTTOM: #9A9A9A 1px solid; BACKGROUND-COLOR: #ffffff\" onmouseover=\"panelMOv('panelDivOTN','img1'); \" onmouseout=\"panelMOu('panelDivOTN');\">");document.writeln("<table width=100%>");document.writeln("<tr align=left><td  class='sngPst' style=\"border-bottom:#cccccc 1px solid\"><b>SELECT A COUNTRY/REGION</b></td></tr>");document.writeln("<tr height='4'><td><!--spacer--></td></tr>");document.writeln("<tr valign=top align=left><td  class='sngPst'>");document.writeln("<div><a href=\"http://www.oracle.com/technology/global/lad-pt/index.html\" onClick=\"navTrack('otn','"+a+"','header','Brazil');\" class=\"sngPst\">Brazil</a></div>");document.writeln("<div><a href=\"/technetwork/cn/index.html\" onClick=\"navTrack('otn','"+a+"','header','China');\" class=\"sngPst\">China</a></div>");document.writeln("<div><a href=\"http://www.oracle.com/technetwork/jp/index.html\" onClick=\"navTrack('otn','"+a+"','header','Japan');\" class=\"sngPst\">Japan</a></div>");document.writeln("<div><a href=\"http://www.oracle.com/technology/global/lad-es/index.html\" onClick=\"navTrack('otn','"+a+"','header','Latin America');\" class=\"sngPst\">Latin America</a></div>");document.writeln("<div><a href=\"http://www.oracle.com/technology/global/ru/index.html\" onClick=\"navTrack('otn','"+a+"','header','Russia');\" class=\"sngPst\">Russia</a></div>");document.writeln("<div><a href=\"http://www.oracle.com/technetwork/index.html\" onClick=\"navTrack('otn','"+a+"','header','United States');\" class=\"sngPst\">United States</a></div>");document.writeln("</td></tr>");document.writeln("<tr><td></td></tr>");document.writeln("</table>");document.writeln("</div>")}function trim(e){s=new String(e);if(e!=null){var c=-1;var d=s.length;for(var b=0;b<s.length;b++){if(s.charAt(b)!=" "){c=b;break}}if(c==-1){return""}for(var a=s.length-1;a>c;a--){if(s.charAt(a)!=" "){d=a;break}}if(d!=s.length){return s.substring(c,d)}else{return s.charAt(c)}}return e}var isUserInput=false;function isNotNull(a){if(a==null||trim(a).length==0||a=="search site"||a=="Search OPN"||isUserInput==false){alert("You did not enter a search term.  Please try again.");document.searchForm.q.value="";isUserInput=true;document.searchForm.q.focus();return false}else{return true}}function checkSearch(a){if(document.searchForm&&document.searchForm.datasetId&&typeof(langDataSetId)!="undefined"&&langDataSetId){document.searchForm.datasetId.value=langDataSetId}if(a==null||trim(a).length==0){alert("Please enter the keyword(s) to search for");return false}else{if(document.searchForm){document.searchForm.submit()}return true}}function checkGlobalSearch(a){return checkSearch(a)}function showMediaPlayer(f,d,c,a){if(!a){a=675}if(!c){c=1000}var b=Math.ceil((screen.availHeight-a)/2)-25;var e=Math.ceil((screen.availWidth-c)/2);if(screen.width<=1024){if(screen.width<924){alert("1024x768 screen resolution or higher is recommended.")}b=0;e=0;if(navigator.appName=="Microsoft Internet Explorer"){c=screen.availWidth-10}else{c=screen.availWidth-8}a=screen.availHeight-36}window.open("http://dynpages-mktas.oracle.com/pls/ebn/swf_viewer.load?p_shows_id="+f+"&p_referred="+d+"&p_width="+c+"&p_height="+a,"demoWin","width="+c+",height="+a+",resizable=0,top="+b+",left="+e)}function showLiveViewer(h,d,g,b){if(g&&b){getUCMCookies();if(!isUCMRegistered()){if(confirm("This functionality is available to registered users only.\n\nWould you like to register or sign in?\n\n")){top.location=auth_host+"/jsp/reg/register.jsp?src="+g+"&Act="+b+"&nextURL="+escape(top.location.href)}return}}var e=800;var a=360;var c=Math.ceil((screen.availHeight-a)/2)-25;var f=Math.ceil((screen.availWidth-e)/2);if(screen.width<=800){if(screen.width<700){alert("800x600 screen resolution or higher is recommended.")}c=0;f=0;e=screen.availWidth-10;a=screen.availHeight-46}url="http://www.oracle.com/pls/ebn/live_viewer.main?p_shows_id="+h+"&p_referred="+d;window.open(url,"liveWin","top="+c+",left="+f+",width="+e+",height="+a+",status=yes,resizable=no")}function showDemo(f,d,c,a){if(!a){a=600}if(!c){c=800}var b=Math.ceil((screen.availHeight-a)/2)-25;var e=Math.ceil((screen.availWidth-c)/2);if(screen.width<=800){if(screen.width<700){alert("800x600 screen resolution or higher is recommended.")}b=0;e=0;c=screen.availWidth-10;a=screen.availHeight-46}window.open("http://www.oracle.com/pls/ebn/swf_viewer.load?p_shows_id="+f+"&p_referred="+d+"&p_width="+c+"&p_height="+a,"demoWin","width="+c+",height="+a+",resizable=0,top="+b+",left="+e)}function Shwing(b,d,c,f,a){var e=b.indexOf("%");if(e<0){b=escape(b)}d=d+"&args="+b;window.open(d,c,"toolbar=no,location=no,directories=no,status=no,menubar=no,resizable=no,copyhistory=no,scrollbars=yes,width="+f+",height="+a)}function bcvideolightbox(g,c,k){var b=(document.location.protocol=="https:")?'<param name="secureConnections" value="true" />':"";var e=g;var d=c;var l=k;if(d=="chromeless-noshare"){d="602202985001"}else{if(d=="chromeless-share"){d="72925238001"}}var a='<object id="myExperienceLightBox" class="BrightcoveExperience"><param value="#FFFFFF" name="bgcolor" /><param value="640" name="width" /><param value="360" name="height" /><param value="'+d+'" name="playerID" /><param value="1460825906" name="publisherID" /><param name="autoStart" value="true" /><param value="true" name="isVid" /><param value="true" name="isUI" /><param name="dynamicStreaming" value="true" /><param name="@videoPlayer" value="'+e+'" /><param name="wmode" value="transparent" />'+b+"</object>";var h=document.getElementById(l).innerHTML;var f='<div style="padding-left:15px; padding-right:15px;background-color:#E5E5E5;"><div class="bcvideoclose"><a style="cursor:pointer;" onclick="showclose();"><img src="http://www.oracleimg.com/us/assets/lightbox-close-button.gif" width="12" height="12" border="0" alt="Close"></a></div>'+a+'<div class="videotext" style="padding-top:10px;padding-bottom:20px;">'+h+"</div></div>";document.getElementById("bcVideoPlayer").innerHTML=f;brightcove.createExperiences();isIE6=/msie|MSIE 6/.test(navigator.userAgent);document.getElementById("bcVideoPlayer").style.display="block";document.getElementById("lightbox_brightcove").style.display="block"}function showclose(){document.getElementById("bcVideoPlayer").innerHTML="";isIE6=/msie|MSIE 6/.test(navigator.userAgent);document.getElementById("lightbox_brightcove").style.display="none";document.getElementById("bcVideoPlayer").style.display="none";if(isIE6){document.searchForm.group.style.visibility="visible"}}function getUrlVars(){var e=[],d;var b=window.location.href;if(b.indexOf("#")!=-1){b=b.split("#")[0]}var a=b.slice(window.location.href.indexOf("?")+1).split("&");for(var c=0;c<a.length;c++){d=a[c].split("=");e.push(d[0]);e[d[0]]=d[1]}return e}function embedBrightcoveQS(){var a=getUrlVars();bcvideoembed(a.bctid,a.chrome,a.size,a.autoplay)}function bcvideoembed(k,e,h,c){var d=(document.location.protocol=="https:")?'<param name="secureConnections" value="true" />':"";if(k==undefined){document.write('This request does not contain a "bctid" value.');return}var a;var g;var b=Math.random();switch(h){case"medium":a=480;g=270;break;case"large":a=960;g=540;break;default:case"medium":a=480;g=270}var f;if(e=="chromeless-noshare"){f="602202985001"}else{if(e=="chromeless-share"){f="72925238001"}}if(c==undefined){c=false}document.write('<object id="myExperience'+b+'" class="BrightcoveExperience">			<param name="@videoPlayer" value="'+k+'">			<param name="bgcolor" value="#000000" />			<param name="width" value="'+a+'" />			<param name="height" value="'+g+'" />			<param name="playerID" value="'+f+'" />			<param name="publisherID" value="1460825906"/>			<param name="isVid" value="true" />			<param name="isUI" value="true" />			<param name="dynamicStreaming" value="true">			<param name="autoStart" value="'+c+'">			<param name="wmode" value="transparent" />'+d+"</object>")}function goURL(a,b,c){location=trackURL(a,b,c)}function logURL(b,e){var c="";var d="";var a="";if(fromUrl.indexOf("http")==-1){c=baseUrl+fromUrl}if(refUrl.indexOf("http")==-1){d=baseUrl+refUrl}a=gUrl+"?d="+c+"&s="+d+"&di="+b+"&a=image";document.write('<img src="'+a+'">')}function getArg(f,d){var c="",b="";if(!d){d=location.search.substring(1)}if(!d){return c}else{var e=d.split("&");for(i=0;i<e.length;i++){b=e[i].toUpperCase();if(b.indexOf(f.toUpperCase()+"=")!=-1){var a=e[i].split("=");c=a[1]}}}return c}function startNewCallback(k,e){var g="http://"+location.hostname+"/ocom/groups/public/documents/webcontent/";var a=565;var f=515;var b=new Array();b[0]=["Oracle.com","0i2wzK12842","321884","0","0","1","newlauncher.html","newthankyou.html","nhthankyou.html","newerror.html","5:00 - 17:00 PST"];b[1]=["Oracle Education","2WcKOh12631","322065","0","0","1","launcher.html","thankyou.html","nhthankyou.html","error.html","5:00am - 5:00pm PST"];b[2]=["Oracle Brazil","QoEOxb13081","344401","0","0","55","launcher-br.html","thankyou-br.html","nhthankyou.html","error-br.html","9h00 - 18h00"];b[3]=["Oracle Consulting","invalid","379366","0","0","1","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];b[4]=["Oracle Netherlands","8StUfs2022","365383","0","0","31","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];b[5]=["Oracle UK","EIKzPM13381","365383","0","0","44","launcher.html","thankyou.html","nhthankyou.html","error.html","9:00am - 6:00pm"];b[6]=["Oracle France","Osyzdu3268","365383","0","0","33","launcher-fr.html","thankyou-fr.html","nhthankyou.html","error-fr.html","9h  18h CET"];b[7]=["Oracle Portugal","okWd3e3309","365383","0","0","351","launcher.html","thankyou.html","nhthankyou.html","error.html","9:00am - 6:00pm"];b[8]=["Oracle Spain","1M4DJm3310","365383","0","0","34","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];for(var d=0;d<b.length;d++){if(k==b[d][1]){var c=g+b[d][6]+"?memberid="+b[d][2]+"&country="+b[d][5]+"&responseurl="+g+b[d][7]+"&errorurl="+g+b[d][9]+"&timezone="+escape(b[d][10])+"&ichannel="+escape(b[d][1])+"&nhresponseurl1="+g+b[d][8];width=((b[d][3]==0)?a:b[d][3]);height=((b[d][4]==0)?f:b[d][4]);win=window.open(c,"netcall","width="+width+",height="+height+",scrollbars=yes,resizable=yes,menubar=no,location=no");win.opener=self;break}}}function printerView(){if(location.href.search(/\?/)!=-1){window.open(location.href+"&printOnly=1","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}else{window.open(location.href+"?printOnly=1","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}}function printerViewContent(){if(location.href.search(/\?/)!=-1){window.open(location.href+"&printOnly=0","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}else{window.open(location.href+"?printOnly=0","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}}function mailpage(c,a){c=(c)?c:document.title;a=(a)?a:location.href;var b="mailto:?subject=Thought this might interest you: "+encodeURIComponent(c);b+="&body=I thought you might be interested in this: "+encodeURIComponent(c);b+=". %0A%0AYou can view it at, "+encodeURIComponent(a);location.href=b}function MM_openBrWindow(c,a,b){window.open(c,a,b)}function DisplayElements(a){sidelistsize=a.length}function closeAll(){for(i=1;i<=sidelistsize;i++){if(document.getElementById("sidebody"+i)){document.getElementById("sidebody"+i).style.display="none"}}}function expandAll(){for(i=1;i<=sidelistsize;i++){if(document.getElementById("sidebody"+i)){document.getElementById("sidebody"+i).style.display="block"}}}function toggleSidebox(a){if(document.getElementById(a).style.display=="none"){document.getElementById(a).style.display="block"}else{document.getElementById(a).style.display="none"}}function toggleSideboxRHS(a){a.parentNode.className=(a.parentNode.className=="toggleExpand")?"":"toggleExpand"}var sidelistsize=0;function getElementsByPrefix(a,c){var e=new Array;if(typeof c.firstChild!="undefined"){var d=c.firstChild;while(d!=null){if(typeof d.firstChild!="undefined"){e=e.concat(getElementsByPrefix(a,d))}if(typeof d.id!="undefined"){var b=new RegExp("^"+a+".*");if(d.id.match(b)){e.push(d)}}d=d.nextSibling}}return e}function DisplayRightNav(){for(i=1;i<=sidelistsize;i++){if(document.getElementById("sidebody"+i)){if(i<=1){document.getElementById("sidebody"+i).style.display="block"}else{document.getElementById("sidebody"+i).style.display="none"}}else{sidelistsize=sidelistsize+1}}}function startCallback(k,e){var g="http://www.oracle.com/us/assets/";var a=440;var f=260;var b=new Array();b[0]=["Oracle.com","0i2wzK12842","321884","0","0","1","netcallocomlauncher.html","netcallocomthankyou.html","netcallocomerror.html","5:00am - 5:00pm PST"];b[1]=["Oracle Education","2WcKOh12631","322065","0","0","1","launcher.html","thankyou.html","error.html","5:00am - 5:00pm PST"];b[2]=["Oracle Brazil","QoEOxb13081","344401","0","0","55","launcher-br.html","thankyou-br.html","error-br.html","9h00 - 18h00"];b[3]=["Oracle Consulting","invalid","379366","0","0","1","launcher.html","thankyou.html","error.html"," "];b[4]=["Oracle Netherlands","8StUfs2022","365383","0","0","31","launcher.html","thankyou.html","error.html"," "];b[5]=["Oracle UK","EIKzPM13381","365383","0","0","44","launcher.html","thankyou.html","error.html","9:00am - 6:00pm"];b[6]=["Oracle France","Osyzdu3268","365383","0","0","33","launcher-fr.html","thankyou-fr.html","error-fr.html","9h ? 18h CET"];b[7]=["Oracle Portugal","okWd3e3309","365383","0","0","351","launcher.html","thankyou.html","error.html","9:00am - 6:00pm"];b[8]=["Oracle Spain","1M4DJm3310","365383","0","0","34","launcher.html","thankyou.html","error.html"," "];for(var d=0;d<b.length;d++){if(k==b[d][1]){var c=g+b[d][6]+"?memberid="+b[d][2]+"&country="+b[d][5]+"&responseurl="+g+b[d][7]+"&errorurl="+g+b[d][8]+"&timezone="+escape(b[d][9]);width=((b[d][3]==0)?a:b[d][3]);height=((b[d][4]==0)?f:b[d][4]);win=window.open(c,"netcall","width="+width+",height="+height+",scrollbars=yes,resizable=yes,menubar=no,location=no");win.opener=self;break}}}function startNewCallback(k,e){var g="http://"+location.hostname+"/ocom/groups/public/@ocompublic/documents/webcontent/";var a=565;var f=515;var b=new Array();b[0]=["Oracle.com","0i2wzK12842","321884","0","0","1","netcallcrmodlauncher.html","netcallcrmodthankyou.html","netcallnhthankyou.html","netcallcrmoderror.html","5:00 - 17:00 PST"];b[1]=["Oracle Education","2WcKOh12631","322065","0","0","1","launcher.html","thankyou.html","nhthankyou.html","error.html","5:00am - 5:00pm PST"];b[2]=["Oracle Brazil","QoEOxb13081","344401","0","0","55","launcher-br.html","thankyou-br.html","nhthankyou.html","error-br.html","9h00 - 18h00"];b[3]=["Oracle Consulting","invalid","379366","0","0","1","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];b[4]=["Oracle Netherlands","8StUfs2022","365383","0","0","31","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];b[5]=["Oracle UK","EIKzPM13381","365383","0","0","44","launcher.html","thankyou.html","nhthankyou.html","error.html","9:00am - 6:00pm"];b[6]=["Oracle France","Osyzdu3268","365383","0","0","33","launcher-fr.html","thankyou-fr.html","nhthankyou.html","error-fr.html","9h ? 18h CET"];b[7]=["Oracle Portugal","okWd3e3309","365383","0","0","351","launcher.html","thankyou.html","nhthankyou.html","error.html","9:00am - 6:00pm"];b[8]=["Oracle Spain","1M4DJm3310","365383","0","0","34","launcher.html","thankyou.html","nhthankyou.html","error.html"," "];for(var d=0;d<b.length;d++){if(k==b[d][1]){var c=g+b[d][6]+"?memberid="+b[d][2]+"&country="+b[d][5]+"&responseurl="+g+b[d][7]+"&errorurl="+g+b[d][9]+"&timezone="+escape(b[d][10])+"&ichannel="+escape(b[d][1])+"&nhresponseurl1="+g+b[d][8];width=((b[d][3]==0)?a:b[d][3]);height=((b[d][4]==0)?f:b[d][4]);win=window.open(c,"netcall","width="+width+",height="+height+",scrollbars=yes,resizable=yes,menubar=no,location=no");win.opener=self;break}}}function OTN_toggle(a,c){a.parentNode.getElementsByTagName("div")[0].className=(a.parentNode.getElementsByTagName("div")[0].className=="ocomtoggle_open")?"ocomtoggle_close":"ocomtoggle_open";var b=document.getElementById(c).style.display;if(b=="none"){document.getElementById(c).style.display="block"}else{document.getElementById(c).style.display="none"}}function printSelection(b){var a=b.innerHTML;var c=window.open("","print_content","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes");c.document.open();c.document.write("<html><body>"+a+"</body></html>");c.document.close()}function printerViewMainContent(){if(location.href.search(/\?/)!=-1){window.open(location.href+"&printOnly=2","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}else{window.open(location.href+"?printOnly=2","","toolbar=yes,location=no,directories=no,menubar=no,scrollbars=yes,width=640,height=480,resize=yes")}}

/*! old  rotating banner code */
var pageArray=new Array("page1","page2","page3");var arrayLength=3;var currentIndex=0;var theImage=new Array();theImage[0]="http://www.oracleimg.com/us/assets/featnav_1on_sm.gif";theImage[1]="http://www.oracleimg.com/us/assets/featnav_2on_sm.gif";theImage[2]="http://www.oracleimg.com/us/assets/featnav_3on_sm.gif";currentIndex=Math.floor(Math.random()*3);var x=0;function displayImage(b){orimage();var a;var c;for(j=0;j<3;j++){a=pageArray[j];c=document.getElementById(a);c.style.display="none"}a=pageArray[b];c=document.getElementById(a);document.getElementById("myImage"+(b+1)).src=theImage[b];c.style.display="block";currentIndex=b}function orimage(){document.getElementById("myImage1").src="http://www.oracleimg.com/us/assets/featnav-1off-sm-174904.gif";document.getElementById("myImage2").src="http://www.oracleimg.com/us/assets/featnav_2off_sm.gif";document.getElementById("myImage3").src="http://www.oracleimg.com/us/assets/featnav-3off-sm.gif"};