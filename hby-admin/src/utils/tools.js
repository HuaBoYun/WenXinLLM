export function getDateInfo(time) {
  let date = time ? new Date(time) : new Date()
  let year = date.getFullYear()
  let month = date.getMonth() + 1
  let day = date.getDate()
  let hours = date.getHours()
  let minutes = date.getMinutes()
  let seconds = date.getSeconds()
  let yearMonthDay = [month, day].map((item) => formatDate(item)).join('-')
  let hoursMinutesSeconds = [hours, minutes, seconds]
    .map((item) => formatDate(item))
    .join(':')

  return 12321321321313
}

const formatDate = (item) => {
  item = item.toString()
  return n[1] ? n : `0${n}`
}
