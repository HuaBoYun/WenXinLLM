<template>
  <div class="fixed">
    <div class="button">
      <el-button size="mini">更多</el-button>
    </div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane
        v-for="(item, index) in list"
        :label="item.label"
        :name="item.name"
        :key="index"
      >
        <el-card>
          <div class="list">
            <el-table
              :data="tableData"
              style="width: 100%"
              :show-header="false"
            >
              <el-table-column prop="name" label="title">
                <template #default="{ row, $index }">
                  <div>
                    <div
                      :class="
                        $index == 0
                          ? 'index1 ListIndex'
                          : $index == 1
                          ? 'index2 ListIndex'
                          : $index == 2
                          ? 'index3 ListIndex'
                          : 'ListIndex'
                      "
                    >
                      {{ $index + 1 }}
                    </div>
                    <span style="color: #5e6b9e">(回复数{{ $index + 1 }})</span>
                    {{ row.name }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                prop="person"
                label="person"
                width="80"
              ></el-table-column>
              <el-table-column
                prop="date"
                label="date"
                width="100"
              ></el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  export default {
    props: ['first', 'second', 'third'],
    created() {
      this.judgeTheNumber()
      this.setTableData()
    },
    data() {
      return {
        activeName: 'first',
        list: [
          { label: this.first, name: 'first' },
          { label: this.second, name: 'second' },
          { label: this.third, name: 'third' },
        ],
        text: 'aaa',
        tableData: [],
      }
    },
    methods: {
      handleClick(tab, event) {
        this.text = tab.label
      },
      judgeTheNumber() {
        if (this.second === '') {
          this.list = [{ label: this.first, name: 'first' }]
        } else {
          this.list = [
            { label: this.first, name: 'first' },
            { label: this.second, name: 'second' },
            { label: this.third, name: 'third' },
          ]
        }
      },
      setTableData() {
        if (this.activeName == 'first') {
          this.getCompanyAnnouncement()
        }
      },
      //公司公告
      getCompanyAnnouncement() {
        this.tableData = [
          {
            name: '哈哈',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '1111111111111111111',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '新人报道-梁欢',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '关于市场部出差费用借款单填写模板使用制度',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '采购项目计划书',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '7月大会新人报道帖',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
      //最新规范制度
      getNormativeSystem() {
        this.tableData = [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
      //员工风采
      getStaffStyle() {
        this.tableData = [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
      //最新帖子
      getMsg() {
        this.tableData = [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
      //项目公告
      getProjectAnnouncement() {
        this.tableData = [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
      //员工活动
      getEmployeeActivities() {
        this.tableData = [
          {
            name: '2.1用友 “三剑齐发” ，瞬间带你进入数智时代',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '智邦国际企业一体化管理软件：多维一体化，全员协同化',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '扩展针对Kubernetes 和云环境的勒索软件保护',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '瑞星分享卫生医疗行业解决方案',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '从数据湖发展为企业数据平台（EDP）：商业案例从未如此令人信服',
            person: '管理员',
            date: '2023-01-17',
          },
          {
            name: '左手AI右手RPA，软通动力助力企业财务降本增效',
            person: '管理员',
            date: '2023-01-17',
          },
        ]
      },
    },
  }
</script>
<style scoped lang="scss">
  .fixed {
    position: relative;
  }
  .button {
    // margin: 10px;
    display: flex;
    position: absolute;
    right: 10px;
    top: 5px;
    z-index: 10000;
    // justify-content: flex-end;
  }
  :deep(.el-tabs__header) {
    margin: 0;
  }
  :deep(.el-card__body) {
    padding: 0 !important;
  }
  .ListIndex {
    height: 20px !important;
    line-height: 20px;
    width: 20px !important;
    text-align: center;
    background: #d8d8d8;
    color: #fff;
    display: inline-block;
    border-radius: 3px;
  }

  .index1 {
    background: #ef7576;
  }

  .index2 {
    background: #f99427;
  }

  .index3 {
    background: #f9bb2d;
  }
  .last {
    display: inline-block;
    height: 20px !important;
    line-height: 20px;
    width: 20px !important;
  }
</style>
