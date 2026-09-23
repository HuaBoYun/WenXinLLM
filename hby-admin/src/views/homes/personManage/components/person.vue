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
          <div class="top">
            <div class="content">
              <div class="left">
                <img :src="url1" alt="" />
              </div>
              <div class="right">
                <div>张三</div>
                <div>财务主管</div>
              </div>
            </div>
            <div class="ask">向他提问</div>
          </div>
          <div class="top">
            <div class="content">
              <div class="left">
                <img :src="url2" alt="" />
              </div>
              <div class="right">
                <div>李四</div>
                <div>审计主管</div>
              </div>
            </div>
            <div class="ask">向他提问</div>
          </div>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
  import icon1 from '../../../../assets/personManage/user1.png'
  import icon2 from '../../../../assets/personManage/user2.png'
  export default {
    props: ['first', 'second'],
    created() {
      this.judgeTheNumber()
      this.setTableData()
    },
    data() {
      return {
        activeName: 'first',
        list: [{ label: this.first, name: 'first' }],
        text: 'aaa',
        tableData: [],
      }
    },
    created() {
      this.url1 = icon1
      this.url2 = icon2
    },
    methods: {
      handleClick(tab, event) {
        this.text = tab.label
      },
      judgeTheNumber() {
        if (this.second === '') {
          this.list = [{ label: this.first, name: 'first' }]
        } else {
          this.list = [{ label: this.first, name: 'first' }]
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
  .content {
    display: flex;
    justify-content: row;
    align-items: center;
  }
  .top {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px 20px;
    img {
      width: 50%;
    }
    &:hover {
      cursor: pointer;
      background-color: #fcf9f9;
      .ask {
        display: block;
        color: #4285f4;
      }
    }
  }
  .ask {
    display: none;
  }
</style>
