import { getSelectedBookInfo } from '@/api/workbench/accountManage'

export default async function getUserSelectedBookInfo() {
  const { data } = await getSelectedBookInfo()
  if (data) {
    localStorage.setItem('bookInfo', JSON.stringify(data))
  }
}
