<template>
  <div id="app" class="page">
    <div>
      <el-tabs v-model="activeName" type="card" @tab-click="handleTabClick">
        <el-tab-pane :label="'待办事宜(' + dbCount + ')'" name="first">
          <dbTable ref="dbTable" @update-count="updateDbCount"></dbTable>
        </el-tab-pane>
        <el-tab-pane :label="'已办事宜(' + ybCount + ')'" name="second">
          <ybTable ref="ybTable" @update-count="updateYbCount"></ybTable>
        </el-tab-pane>
        <el-tab-pane :label="'我发起的(' + fqCount + ')'" name="third">
          <fqTable ref="fqTable" @update-count="updateFqCount"></fqTable>
        </el-tab-pane>
        <el-tab-pane :label="'抄送事宜(' + csCount + ')'" name="fourth">
          <csTable ref="csTable" @update-count="updateCsCount"></csTable>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
  import dbTable from '@/views/fwgl/home/components/wdgz/db.vue'
  import ybTable from '@/views/fwgl/home/components/wdgz/yb.vue'
  import fqTable from '@/views/fwgl/home/components/wdgz/fq.vue'
  import csTable from '@/views/fwgl/home/components/wdgz/cs.vue'

  export default {
    name: 'Download',
    components: {
      dbTable,
      ybTable,
      fqTable,
      csTable,
    },
    data() {
      return {
        pageSizes: [5, 10, 15, 20, 50, 100],
        layout: 'total, prev, pager, next, jumper',
        tableData: [
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
          {
            date: 'XXXX',
            name: 'XXXX',
            address: 'XXXX',
          },
        ],
        title: '',
        // tab 数量
        dbCount: 0,
        ybCount: 0,
        fqCount: 0,
        csCount: 0,

        tableData3: [
          {
            number: 'CJTZ01',
            content: '企业增加注册资本或者资本公积时是否计提印花税',
          },
          {
            number: 'RULE-01',
            content: '发运数量不能低于50',
          },
        ], //预计结果
        activeName: 'first',
        activeName2: 'first',
        activeName3: 'first',
        activeName4: 'first',
        activeYear: 2021,
        years: [2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021],
      }
    },

    created() {
      this.getDefaultInfo()
    },
    methods: {
      changeActiveYear: function (year) {
        this.activeYear = year
      },

      filterHandler(value, row, column) {
        const property = column['date']
        return row[property] === value
      },

      /**
       * @description: 改变每一页请求数量
       * @param {*} val
       * @return {*}
       */
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      /**
       * @description: 跳转页数
       * @param {*} val
       * @return {*}
       */
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      // 更新各 tab 数量
      updateDbCount(count) {
        this.dbCount = count || 0
      },
      updateYbCount(count) {
        this.ybCount = count || 0
      },
      updateFqCount(count) {
        this.fqCount = count || 0
      },
      updateCsCount(count) {
        this.csCount = count || 0
      },
      // tab切换事件
      handleTabClick(tab) {
        switch (tab.name) {
          case 'first':
            this.$refs.dbTable && this.$refs.dbTable.fetchData1()
            break
          case 'second':
            this.$refs.ybTable && this.$refs.ybTable.fetchData1()
            break
          case 'third':
            this.$refs.fqTable && this.$refs.fqTable.fetchWfqdData()
            break
          case 'fourth':
            this.$refs.csTable && this.$refs.csTable.fetchWfqdData()
            break
        }
      },
    },
  }
</script>
<style scoped>
  h5 {
    font-size: 18px;
    margin: 2px;
    color: #333;
  }
  .el-col > div {
    border: 1px solid #dcdfe5;
    margin-bottom: 10px;
  }

  .page {
    padding: 20px;
  }

  .table-title {
    cursor: pointer;
  }

  .table th {
    position: relative;
  }

  .table-filter {
    position: absolute;
    border: 1px solid gainsboro;
    padding: 5px;
    left: 0;
    right: 0;
    top: 40px;
    background: white;
    min-width: 160px;
  }

  .table-filter input {
    padding: 5px;
    font-size: 14px;
    margin-right: 5px;
  }

  .table-filter .form-check {
    display: flex;
    flex-direction: column;
    text-align: left;
    font-size: 14px;
    font-weight: 400;
    padding: 5px;
  }

  .table-filter button {
    font-size: 12px;
    padding: 2px 10px;
  }

  .table-responsive {
    background: white;
    padding: 20px;
    margin-bottom: 20px;
  }

  .chats > div > div {
    background: white;
    padding: 10px;
    margin-bottom: 20px;
  }

  .select-year {
    display: flex;
    background: aliceblue;
    padding: 5px;
  }

  .select-year > div {
    margin-right: 10px;
    padding: 2px 5px;
    cursor: pointer;
  }

  .select-year .active {
    background: #ffaf0f;
    border-radius: 20px;
    color: white;
  }
  h5 {
    margin: 0 0 10px 0;
    font-size: 17px;
  }
  .title {
    font-size: 24px;
    padding: 10px;
    border: 1px solid #dcdfe5;
    margin-bottom: 30px;
  }
  /* .el-tab-pane {
    height: 300px;
  } */
</style>
