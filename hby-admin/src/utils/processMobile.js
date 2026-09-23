export function wddbDialogStyleFunc() {
  const dialogWarpper = document.getElementsByClassName('wddb_dialog')
  const elDialog = dialogWarpper[0].children
  const elDialogBody = elDialog[0].getElementsByClassName('el-dialog__body')
  dialogWarpper[0].style.overflow = 'hidden'
  elDialog[0].style.cssText = 'margin: 0 !important;width: 100%;display: flex;flex-direction: column;height: 100%;'
  elDialogBody[0].style.cssText = 'flex: 1;overflow: auto;'
}

export function listPageStyleFunc() {
  const page = document.getElementsByClassName('vue-admin-beautiful-wrapper')
  page[0].style.cssText = 'display: none !important;'
}

export function changeFormSizeStyleFunc() {
  const elFormItemNode = document.getElementsByClassName('el-form-item')
  for (let i = 0; i < elFormItemNode.length; i++) {
    if (elFormItemNode[i] && elFormItemNode[i].style) {
      elFormItemNode[i].style.marginBottom = '4px'
    }
  }
}