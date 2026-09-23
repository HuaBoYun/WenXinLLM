// 去重并格式化为嵌套children类型数据
function dataNoRepeat(listData) {
  const firstList = []
  // 第一部分去重
  listData.forEach((x) => {
    const findObj = firstList.find((y) => y.firstId === x.firstId)
    if (findObj) {
      findObj.children
        ? findObj.children.push(x)
        : (findObj.children = [x])
    } else {
      firstList.push({
        firstId: x.firstId,
        examineEmphasis: x.examineEmphasis,
        children: [x],
      })
    }
  })

  // 第二部分去重
  firstList.forEach((fItem) => {
    const secondList = []
    fItem.children.forEach((x) => {
      const findObj = secondList.find(
        (y) => y.secondContent === x.secondContent
      )
      if (findObj) {
        findObj.children
          ? findObj.children.push(x)
          : (findObj.children = [x])
      } else {
        secondList.push({
          secondContent: x.secondContent,
          children: [x],
        })
      }
    })

    fItem.children = JSON.parse(JSON.stringify(secondList))
  })

  return JSON.parse(JSON.stringify(firstList))
}

// 格式化合并行
function formatRowSpan(listData) {
  const newListData = []

  listData.forEach((f, fi) => {
    let count1 = 0
    f.children.forEach((c, ci) => {
      count1 += c.children.length
    })

    f.children.forEach((c, ci) => {
      c.children.forEach((t, ti) => {
        let secondLen = c.children.length

        if (t.examineType === 1) {
          t.rowSpan = ti === 0 ? [secondLen, secondLen, 1] : [0, 0, 1]
          t.colSpan = [1, 1, 1, 1]
        } else {
          t.rowSpan =
            ti === 0
              ? [secondLen, secondLen, secondLen, secondLen, 1]
              : [0, 0, 0, 0, 1]
          t.colSpan = [1, 1, 1, 1, 1, 1]
        }

        let cacheCount1 = count1
        if (ci === 0 && ti === 0) {
          t.examineType === 1 ? t.rowSpan.unshift(cacheCount1) : t.rowSpan.unshift(cacheCount1, cacheCount1)
          cacheCount1 = 0
        } else {
          t.examineType === 1 ? t.rowSpan.unshift(0) : t.rowSpan.unshift(0, 0)
        }

        t.INDEX = fi
        newListData.push(t)
      })
    })
  })

  return JSON.parse(JSON.stringify(newListData))
}

export { dataNoRepeat, formatRowSpan }
