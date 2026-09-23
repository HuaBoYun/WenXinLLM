var userAgent = navigator.userAgent,
  rMsie = /(msie\s|trident.*rv:)([\w.]+)/,
  rFirefox = /(firefox)\/([\w.]+)/,
  rOpera = /(opera).+version\/([\w.]+)/,
  rChrome = /(chrome)\/([\w.]+)/,
  rSafari = /version\/([\w.]+).*(safari)/;
var browser;
var version;
var ua = userAgent.toLowerCase();
function uaMatch(ua) {
  var match = rMsie.exec(ua);
  if (match != null) {
    return { browser: "IE", version: match[2] || "0" };
  }
  var match = rFirefox.exec(ua);
  if (match != null) {
    return { browser: match[1] || "", version: match[2] || "0" };
  }
  var match = rOpera.exec(ua);
  if (match != null) {
    return { browser: match[1] || "", version: match[2] || "0" };
  }
  var match = rChrome.exec(ua);
  if (match != null) {
    return { browser: match[1] || "", version: match[2] || "0" };
  }
  var match = rSafari.exec(ua);
  if (match != null) {
    return { browser: match[2] || "", version: match[1] || "0" };
  }
  if (match != null) {
    return { browser: "", version: "0" };
  }
  return { browser: "safari", version: "13.0.3" };
}
var browserMatch = uaMatch(userAgent.toLowerCase());
if (browserMatch.browser) {
  browser = browserMatch.browser;
  version = browserMatch.version;
}

var str = '';

// var copyright = '金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAKkBAACwAQAALAAAAL0DsOHLA1iADeyV2QqJP637WrfyJs9zHSMFOzpSrPc6aFuj5r3ywWX8uEqRNA7X8RdmM6LC3qfYkKz8qV3bA+a44qclQarvp64ubw7VqAKx1w6VkjR31A/OUGbqutGlsMuQYq0MI68vMQGXnUG6CIXuaBl0PpzAczH6QFa5jARw7cjMCqQmPMOUwXZ6tAX6YBwdigPwSEUv3CXO2ZqPkzwn/j3YYozNwvRTnbhO7nbmKM+E9RPXj2+v1/Xc06dPDhnogXsxzMQpjclCoYrJMEkzdPgS8UqviR0lhsKJ41XHCeWPqYu8PY0EbgZvwaDHyrRiQqYGyutn6vrepjnvxehRBTexZ3leCrrJabd0A55KAAkjOAPIn48YZZ2E9stAkymq1l4MDdk7PZlxhmWu35iHeDUVGrp2TDIZTPY+HA6ZYYcOqWh4kRSyskO8phPqNw+4kaKs4SF+HvWDyL2suQGOwDic4Atyb2Ae6GmOKNs6I6sfw0cumR67rm8FHhcWlBhIroyC7lThCOI5CH2FHiVuB4hlG2JNqVxZ6kdMP8eWs8X7OrVwvtqk+0mKxhjhLHieSyYI2cZ4tkNSdbRYKuE=';
// var copyright='金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAJEBAACgAQAALAAAAEnETMLfjzlFKkLbsBP8QVd2P5OtkkDyMgxgC1pFIRacyXbx8diNwmCITUtMwEDfDyAdCEVVr6yXCHD97ZiaT8YIZCNeE4A7gs3HY2tM8sspVWCd30/CiZaDUx0AsqcXEEXoB/vsqnZaET0u1wGsphKe8ZKcvJJJdQ3Ciy2cTiQObN//pSPY/3LgdxdJeEyC5ibUvVW9ngjJG0brXt4N17r1TDygcaC+SZ/pBTLg3Hac6bHoHHks5P9R5HvAjICsShbtkO2snAIvvslWn0BSDoL4XRSKyUErCdOwgUs+QjQ7HkoziQI+YpszJD05dP2AxH39S8feWlTg0D9ouIEHTCQH0KNvzaqKc2c2dYqrHqY77SJ9JRozb9SsqruQvUBo/CG91rVivzUiZrybJ14KjepAVO1mAmbCzbSYMFtGFWlBIwN6NbNYBtD+jZ4EB2dLs+V7dQpbR7J/EZVI2Sg8npwls3b9DTxkaNOubL7CkvTNfpETB8Irmq8kkWYzGRrv0iOKgRvkwm8AnQqliHCzRdvytzmoaUVhjEiTMjbEMYkovu1U1W2cVLD4jxA1xcDiXw==';
// var copyright='金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAJkBAACgAQAALAAAAAPqA7JoMIvE+uq+SspMfHcoiOvDTjFYQ4dzg8uHSbvnOmOjl0rROYD679MUdJulNM3AqskKNTqrUcBlxUH4Aj6rhrXNu2KS1SGhuArz6EXhIiC3GqzzXArmUThfWovovdMFTG6s1703RabIFNzZ0Srn8CLIs5rf4JLVDGo2IYV0LqqZMtC0zR3aIetIghnNBp87YZNAZt8Sg1ZXePskUTVnYmuzFD7LJUFHaba6otbpfU5JHQw04UJ1ulcmsKBanjKcjptpWo9IY7Fz1d58otDF0W2JMQEmPMZ1pizu76tzyPA4CPHl9zUVny8kjtnGSL3tFplmi0wMkc2L+6QB1P4fgq4yNcKzILFsKN/cIZRdHc86qt0+Zo6EhApfENc65f5WvvkC25HSSjlAUQw4T+xi8QP/qUa+Jkzmf/iJzy4NSHv/H991LUzGfxGfcR3/QT8TljDyTibtW/o5ECQw7j56ldEzjQiTlUX+PnvNSt50QASN9Pg1wV2XlscharLodDqRMjXVSkBsGq7VodN7Y/sX3O//LCKKHqBh2H5e070f3fQaE4EF7K7Q7iPBcqtLMg==';
// var copyright='金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAJkBAACgAQAALAAAAAPqA7JoMIvE+uq+SspMfHcoiOvDTjFYQ4dzg8uHSbvnOmOjl0rROYD679MUdJulNM3AqskKNTqrUcBlxUH4Aj6rhrXNu2KS1SGhuArz6EXhIiC3GqzzXArmUThfWovovdMFTG6s1703RabIFNzZ0Srn8CLIs5rf4JLVDGo2IYV0LqqZMtC0zR3aIetIghnNBp87YZNAZt8Sg1ZXePskUTVnYmuzFD7LJUFHaba6otbpfU5JHQw04UJ1ulcmsKBanjKcjptpWo9IY7Fz1d58otDF0W2JMQEmPMZ1pizu76tzyPA4CPHl9zUVny8kjtnGSL3tFplmi0wMkc2L+6QB1P4fgq4yNcKzILFsKN/cIZRdHc86qt0+Zo6EhApfENc65f5WvvkC25HSSjlAUQw4T+xi8QP/qUa+Jkzmf/iJzy4NSHv/H991LUzGfxGfcR3/QT8TljDyTibtW/o5ECQw7j56ldEzjQiTlUX+PnvNSt50QASN9Pg1wV2XlscharLodDqRMjXVSkBsGq7VodN7Y/sX3O//LCKKHqBh2H5e070f3fQaE4EF7K7Q7iPBcqtLMg==';
// var copyright = "示例云（北京）技术有限公司[专用];V5.0S0xGAAEAAAAAAAAAEAAAAEgBAABQAQAALAAAABh7mrTeC5UgyYEKI+InMhmhfkwestU5+Bs+0awkPV99KueYvRTvqy4popPU+DjPg6xpUzBqdV35BJ2S0LTeYM7cjULVOXyDxoTyO7HuIB8Yjx8+Q2UXBAny2vi2+h15nz8wrPnzMzpBkJhrnzqWajPh6PWYBBFaFveY7PStP4jjzDVlsIjVP+Rt4jg0YCx+oPVqIHVob4+ppuT+5qEu2AVNMOAa74cGUCZaEwbvXEucwY8nLMAkKc8cxmr2i28BEVC7UmSgM8idaTFZWFZQtCl1pIS/wlSGX4gOAs2J4aSduZEFNO5Wf/tsHHS3IainKMBB4eGBMiR1nm7nfE9HwQhMzDsBUOuIxX6/wnf6J1LnJu5VT4Flipz3aRqJNtOrnJp6GtTCQw1JDwY4O5QzCOck7Fqgd7UIypJIqOSfWow1EVeDOnA9vdP09Ia7wAmkI/eN4xsnUfPOO3MpHBntVlY=";
// var copyright_zukk = "示例云（北京）技术有限公司[专用];V5.0S0xGAAEAAAAAAAAAEAAAAEgBAABQAQAALAAAABh7mrTeC5UgyYEKI+InMhmhfkwestU5+Bs+0awkPV99KueYvRTvqy4popPU+DjPg6xpUzBqdV35BJ2S0LTeYM7cjULVOXyDxoTyO7HuIB8Yjx8+Q2UXBAny2vi2+h15nz8wrPnzMzpBkJhrnzqWajPh6PWYBBFaFveY7PStP4jjzDVlsIjVP+Rt4jg0YCx+oPVqIHVob4+ppuT+5qEu2AVNMOAa74cGUCZaEwbvXEucwY8nLMAkKc8cxmr2i28BEVC7UmSgM8idaTFZWFZQtCl1pIS/wlSGX4gOAs2J4aSduZEFNO5Wf/tsHHS3IainKMBB4eGBMiR1nm7nfE9HwQhMzDsBUOuIxX6/wnf6J1LnJu5VT4Flipz3aRqJNtOrnJp6GtTCQw1JDwY4O5QzCOck7Fqgd7UIypJIqOSfWow1EVeDOnA9vdP09Ia7wAmkI/eN4xsnUfPOO3MpHBntVlY=";

var copyright = "浙江省国有资本运营有限公司[专用];V5.0S0xGAAEAAAAAAAAAEAAAAEgBAABQAQAALAAAAIxpKgEWqYLW4+TmRLsDMJ4eTuinOYNMjSFeuXn/Lu5nvsnF0m+MsqI6gKUP0CcNHS4MNLlDFuqhkH+7nhoyuCDi2IuhAWn6/XmKfUmmATX9MLURPovbIwhZATlfUzqAw9hPLJjloC7aL+7qGh0iu7WJ/KAUUFYLwud87nn5PdcvcTpa53nRk0akcSJZPia9Oeqt+rNkwNny/5ot00IIj/RSLBL4VDXKGw7Tr9C6oNN981intqHrObyMkVF0PfI2nBJjcYczPWpEVy3Q2LIs3NuvgkIboJLdn7I4Seem0fy2W8AVk1pNcOeOXrwj19edkS7JZX6wG8dejoooVqxS/jTfw9E0LqXfS+ICDszpHvAPHNTfiEmdOGykKb2XtRQwXRcQFfIvjQauuhpdsioNrvhBfkqZ+diPMK8Eq2zzZJOaBbsFF1ri6u+Z55IaT/p4Y3NG2lOjQSuK+NnO9czn1dA="
var copyright_zukk = "浙江省国有资本运营有限公司[专用];V5.0S0xGAAEAAAAAAAAAEAAAAEgBAABQAQAALAAAAIxpKgEWqYLW4+TmRLsDMJ4eTuinOYNMjSFeuXn/Lu5nvsnF0m+MsqI6gKUP0CcNHS4MNLlDFuqhkH+7nhoyuCDi2IuhAWn6/XmKfUmmATX9MLURPovbIwhZATlfUzqAw9hPLJjloC7aL+7qGh0iu7WJ/KAUUFYLwud87nn5PdcvcTpa53nRk0akcSJZPia9Oeqt+rNkwNny/5ot00IIj/RSLBL4VDXKGw7Tr9C6oNN981intqHrObyMkVF0PfI2nBJjcYczPWpEVy3Q2LIs3NuvgkIboJLdn7I4Seem0fy2W8AVk1pNcOeOXrwj19edkS7JZX6wG8dejoooVqxS/jTfw9E0LqXfS+ICDszpHvAPHNTfiEmdOGykKb2XtRQwXRcQFfIvjQauuhpdsioNrvhBfkqZ+diPMK8Eq2zzZJOaBbsFF1ri6u+Z55IaT/p4Y3NG2lOjQSuK+NnO9czn1dA="


// var copyright='金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAKkBAACwAQAALAAAAL0DsOHLA1iADeyV2QqJP637WrfyJs9zHSMFOzpSrPc6aFuj5r3ywWX8uEqRNA7X8RdmM6LC3qfYkKz8qV3bA+a44qclQarvp64ubw7VqAKx1w6VkjR31A/OUGbqutGlsMuQYq0MI68vMQGXnUG6CIXuaBl0PpzAczH6QFa5jARw7cjMCqQmPMOUwXZ6tAX6YBwdigPwSEUv3CXO2ZqPkzwn/j3YYozNwvRTnbhO7nbmKM+E9RPXj2+v1/Xc06dPDhnogXsxzMQpjclCoYrJMEkzdPgS8UqviR0lhsKJ41XHCeWPqYu8PY0EbgZvwaDHyrRiQqYGyutn6vrepjnvxehRBTexZ3leCrrJabd0A55KAAkjOAPIn48YZZ2E9stAkymq1l4MDdk7PZlxhmWu35iHeDUVGrp2TDIZTPY+HA6ZYYcOqWh4kRSyskO8phPqNw+4kaKs4SF+HvWDyL2suQGOwDic4Atyb2Ae6GmOKNs6I6sfw0cumR67rm8FHhcWlBhIroyC7lThCOI5CH2FHiVuB4hlG2JNqVxZ6kdMP8eWs8X7OrVwvtqk+0mKxhjhLHieSyYI2cZ4tkNSdbRYKuE='
// var copyright_zukk='金格科技iWebOffice2015智能文档中间件[演示版];V5.0S0xGAAEAAAAAAAAAEAAAAKkBAACwAQAALAAAAL0DsOHLA1iADeyV2QqJP637WrfyJs9zHSMFOzpSrPc6aFuj5r3ywWX8uEqRNA7X8RdmM6LC3qfYkKz8qV3bA+a44qclQarvp64ubw7VqAKx1w6VkjR31A/OUGbqutGlsMuQYq0MI68vMQGXnUG6CIXuaBl0PpzAczH6QFa5jARw7cjMCqQmPMOUwXZ6tAX6YBwdigPwSEUv3CXO2ZqPkzwn/j3YYozNwvRTnbhO7nbmKM+E9RPXj2+v1/Xc06dPDhnogXsxzMQpjclCoYrJMEkzdPgS8UqviR0lhsKJ41XHCeWPqYu8PY0EbgZvwaDHyrRiQqYGyutn6vrepjnvxehRBTexZ3leCrrJabd0A55KAAkjOAPIn48YZZ2E9stAkymq1l4MDdk7PZlxhmWu35iHeDUVGrp2TDIZTPY+HA6ZYYcOqWh4kRSyskO8phPqNw+4kaKs4SF+HvWDyL2suQGOwDic4Atyb2Ae6GmOKNs6I6sfw0cumR67rm8FHhcWlBhIroyC7lThCOI5CH2FHiVuB4hlG2JNqVxZ6kdMP8eWs8X7OrVwvtqk+0mKxhjhLHieSyYI2cZ4tkNSdbRYKuE='
// var copyright_zukk = '金格科技iWebOffice信创中间件[演示];V5.0S0xGAAEAAAAAAAAAEAAAAEgBAABQAQAALAAAAGDV3U7sOUhHpD7WGljlLz3rFC0B82+CKBqCFyjY6jqEVw2UaQ1PYAHyHhj2XHhSTYxTiL5CVQrCjQIFhjMKiCX0GCTA6XR7FS5S7FV9C6KH1WRz25hrmhoIi+m/C+jv/PRYI+tK5ZAUra4cbFbbgbK0UMg1c/bJLDxet/Odbfk0Cq3UUpoPohA8cmU/f6oQKhkcMQ06+yNtTn6sbq8Il9Bd9IX3CDPcfNew2pC7DlPfQb9zVwemncjToJ3COVGenQ/fDhEfsMvPY0Si1PsxYhJTi1SpVwut7Cw0oghqEXXhFyB5z8/2KhSOH7pq7RcJgskDTMJgwXmE9sYGe+zc6Init/8x7qD7MmEVyhlaIcL6gQMjY1HYrxWmSzsVi63VW2Rz7LGtXr/EWjGBckniFrstPin8q1u+wbJltf588HP5r+hbchwLmf9I3ujHufOWSCsjdkfxH+REI4gs60LTKKc=';



if ((window.ActiveXObject != undefined) || (window.ActiveXObject != null) || "ActiveXObject" in window) {
  str += '<object id="WebOffice2015" ';

  str += ' width="100%"';
  str += ' height="100%"';
  //32位控件
  if (window.navigator.platform == "Win32")
    str += ' CLASSID="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"  codebase="iWebOffice2015.cab#version=12,7,0,828"';
  //64位控件
  if (window.navigator.platform == "Win64")
    str += ' CLASSID="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D024"  codebase="iWebOffice2015.cab#version=12,5,0,652"';
  str += '>';
  str += '<param name="Copyright" value="' + copyright + '">';
}
else if (browser == "chrome") {
  str += '<object id="WebOffice2015" ';


  str += ' width="100%"';
  str += ' height="100%"';
  str += ' clsid="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"';
  str += ' type="application/kg-plugin"';                              //KGChromePlugin插件type
  //str += ' type="application/kg-activex"';                           //iWebPlugin插件type
  //str += ' type="application/iwebplugin"';
  str += ' OnCommand="OnCommand"';
  str += ' OnOLECommand="OnOLECommand"';
  str += ' OnReady="OnReady"';
  str += ' OnRightClickedWhenAnnotate="OnRightClickedWhenAnnotate"';
  str += ' OnSending="OnSending"';
  str += ' OnSendEnd="OnSendEnd"';
  str += ' OnRecvStart="OnRecvStart"';
  str += ' OnRecving="OnRecving"';
  str += ' OnRecvEnd="OnRecvEnd"';
  str += ' OnFullSizeBefore="OnFullSizeBefore"';
  str += ' OnFullSizeAfter="OnFullSizeAfter"';
  str += ' Copyright="' + copyright + '"';
  str += '>';
}
else if (browser == "firefox") {
  str += '<object id="WebOffice2015" ';
  str += ' width="100%"';
  str += ' height="1000px"';
  str += ' clsid="CLSID:D89F482C-5045-4DB5-8C53-D2C9EE71D025"';
  str += ' type="application/kg-activex"';
  //str += ' type="application/iwebplugin"';
  str += ' OnCommand="OnCommand"';
  str += ' OnReady="OnReady"';
  str += ' onUnload="onUnload"';
  str += ' OnOLECommand="OnOLECommand"';
  str += ' OnExecuteScripted="OnExecuteScripted"';
  str += ' OnQuit="OnQuit"';
  str += ' OnSendStart="OnSendStart"';
  str += ' OnSending="OnSending"';
  str += ' OnSendEnd="OnSendEnd"';
  str += ' OnRecvStart="OnRecvStart"';
  str += ' OnRecving="OnRecving"';
  str += ' OnRecvEnd="OnRecvEnd"';
  str += ' OnRightClickedWhenAnnotate="OnRightClickedWhenAnnotate"';
  str += ' OnFullSizeBefore="OnFullSizeBefore"';
  str += ' OnFullSizeAfter="OnFullSizeAfter"';
  str += ' Copyright="' + copyright + '"';
  str += '>';
}

str += '</object>';

var str_zukk = '<object id="WebOffice" type="application/iweboffice" height="1000px" width="100%" >'
str_zukk += '<param name="Copyright" value="' + copyright_zukk + '">';
str_zukk += '</object>';


//alert(OSType.indexOf("Win"))
var OSType = detectOS()
function LoadStr() {
  if (OSType.indexOf("Win") > -1) {
    //document.write(str);
    //alert(str);
    return str;
  } else {
    //alert(str_zukk);
    return str_zukk;
    //document.write(str_zukk);
  }
}

function detectOS() {
  var sUserAgent = navigator.userAgent;
  //alert("navigator.userAgent" + navigator.userAgent);
  //alert("navigator.platform" + navigator.platform);

  var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
  var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");

  if (isMac)
    return "Mac";

  var isUnix = (navigator.platform == "X11") && !isWin && !isMac;

  if (isUnix)
    return "Unix";

  var isLinux = (String(navigator.platform).indexOf("Linux") > -1);

  if (isLinux)
    return "Linux";

  if (isWin) {

    return "isWin";

    var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;
    if (isWin2K)
      return "Win2000";

    var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 || sUserAgent.indexOf("Windows XP") > -1;
    if (isWinXP)
      return "WinXP";

    var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;
    if (isWin2003)
      return "Win2003";

    var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
    if (isWinVista)
      return "WinVista";

    var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
    if (isWin7)
      return "Win7";

    var isWin10 = sUserAgent.indexOf("Windows NT 10.0") > -1 || sUserAgent.indexOf("Windows 10") > -1;
    if (isWin10)
      return "isWin10";
  }

  return "other";
}

/////////////////////////////////////////////////////////////////////
//const WebOfficeStr = str
export {
  LoadStr, detectOS
}

