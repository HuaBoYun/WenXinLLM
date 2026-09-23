/*
 * @Author: 康某 dev@example.com
 * @Date: 2022-10-04 10:40:07
 * @LastEditors: 康某 dev@example.com
 * @LastEditTime: 2022-10-04 15:15:35
 * @FilePath: \hb-admin\src\mixis\index.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
//  查询条件方法混入 以及表格过滤混入

export const searchTableMixis = {
  data() {
    return {
      filedAll: [], //所有表格项
      filedNow: [], //当前表格项
      // searchAll: this.getFiled(), //所有搜索项
      searchNow: [], //当前所有搜索项
      searchItem: [], //可见搜索项
      localKey: '',
      tableKey: '',
      searchMore: true,
    }
  },
  methods: {
    initSearch() {
      let self = this
      this.$nextTick(function () {
        let data = localStorage.getItem(self.localKey)
        if (data) {
          data = JSON.parse(data)
          let tempArr = []
          for (let i = 0; i < data.length; i++) {
            if (data[i].show) {
              tempArr.push(data[i])
            }
          }
          this.searchNow = tempArr
        } else {
          this.searchNow = this.searchAll
        }
        // 重置非展示搜索项
        this.searchAll.forEach((x) => {
          if (!this.searchNow.some((y) => y.key === x.key)) {
            if (Array.isArray(this.queryForm[x.key])) {
              this.$set(this.queryForm, x.key, [])
              // this.queryForm[x.key] = []
            } else if (this.queryForm[x.key] instanceof Object) {
              this.$set(this.queryForm, x.key, {})
            } else {
              this.$set(this.queryForm, x.key, null)
            }
          }
        })

        if (this.searchMore) {
          this.searchItem = this.searchNow
        } else {
          this.searchItem = this.searchNow.slice(0, 4)
        }
      })
    },
    showMore() {
      this.searchMore = !this.searchMore
      console.log(this.searchMore)
      if (this.searchMore) {
        this.searchItem = this.searchNow
      } else {
        this.searchItem = this.searchNow.slice(0, 4)
      }
    },
    // 动态表格开始
    initTable() {
      this.loading = true
      let self = this
      this.$nextTick(function () {
        let data = localStorage.getItem(self.tableKey)
        if (data) {
          data = JSON.parse(data)
          let tempArr = []
          for (let i = 0; i < data.length; i++) {
            if (data[i].show) {
              tempArr.push(data[i])
            }
          }
          this.filedNow = tempArr
        } else {
          this.filedNow = this.filedAll
        }
        this.loading = false
      })
    },
  },
}
