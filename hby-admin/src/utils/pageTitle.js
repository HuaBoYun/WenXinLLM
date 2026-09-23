import { titleReverse, titleSeparator } from '@/config'
import store from '@/store'
import i18n from '@/i18n'

/**
 * @description 设置标题
 * @param pageTitle
 * @returns {string}
 */
export default function getPageTitle(pageTitle) {
  const title = store.getters['settings/title']
  if (i18n.te(`vabI18n.${pageTitle}`))
    pageTitle = i18n.t(`vabI18n.${pageTitle}`)
  let newTitles = []
  if (pageTitle) newTitles.push(pageTitle)
  if (title) newTitles.push(title)
  if (titleReverse) newTitles = newTitles.reverse()
  return newTitles.join(titleSeparator)
}
