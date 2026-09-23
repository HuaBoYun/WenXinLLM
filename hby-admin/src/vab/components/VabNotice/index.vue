<template>
  <el-badge v-if="theme.showNotice" :value="badge">
    <!--    <vab-icon icon="notification-line" />-->
    <el-popover
      placement="bottom"
      trigger="hover"
      width="380"
      popper-class="notice-popover"
      @show="handleNoticeShow"
    >
      <template #reference>
        <vab-icon icon="notification-line" />
      </template>
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane
          v-for="(item, index) in tabList"
          :key="index"
          :label="translateTitle(item.title)"
          :name="item.component"
        >
          <div v-if="activeName === 'wddb'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemdb, indexdb) in list"
                  :key="indexdb"
                  @click="showWddb(itemdb)"
                >
                  <span v-html="itemdb.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'wdcy'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemcy, indexcy) in cyList"
                  :key="indexcy"
                  @click="showWdcy(itemcy)"
                >
                  <span v-html="itemcy.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'wfqd'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemfq, indexfq) in fqList"
                  :key="indexfq"
                  @click="showWfqd(itemfq)"
                >
                  <span v-html="itemfq.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'cssy'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemsy, indexsy) in syList"
                  :key="indexsy"
                  @click="showCssy(itemsy)"
                >
                  <span v-html="itemsy.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'dfsy'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemfq, indexfq) in dfList"
                  :key="indexfq"
                  @click="showDfsy(itemfq)"
                >
                  <span v-html="itemfq.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'bhsy'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemfq, indexfq) in bhList"
                  :key="indexfq"
                  @click="showBhsy(itemfq)"
                >
                  <span v-html="itemfq.fullName" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'xxdb'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemxx, indexxx) in xxdbList"
                  :key="indexxx"
                  @click="showXxdb(itemxx)"
                >
                  <span v-html="itemxx.distributionTitle" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
          <div v-if="activeName === 'wysx'" class="notice-list">
            <el-scrollbar>
              <ul>
                <li
                  style="cursor: pointer"
                  v-for="(itemwy, indexwy) in wyList"
                  :key="indexwy"
                  @click="showWysx(itemwy)"
                >
                  <span v-html="itemwy.distributionTitle" />
                </li>
              </ul>
            </el-scrollbar>
          </div>
        </el-tab-pane>
      </el-tabs>
      <div class="notice-clear" @click="toMsg">
        <el-button type="text">
          <vab-icon icon="more-fill" />
          <span>{{ translateTitle('查看更多') }}</span>
        </el-button>
      </div>
    </el-popover>
    <Views ref="edit"></Views>
    <!-- 整改方案详情 -->
    <scheme-info
      v-if="showDialog"
      ref="edit"
      @closeDialog="showDialog = false"
    />
  </el-badge>
</template>

<script>
  import Views from '@/views/oilAudit/plan/components/xmqdEdit.vue'
  import {
    my_circulation1,
    my_faqi,
    my_shiyi,
    getToDoList,
    getRemindList,
    getZGInfo,
    getAllMsgNum,
  } from '@/api/setting/msg'
  import {
    getDistributionListPage,
    modifyDistributionInfo,
  } from '@/oapi/setting/system'
  import { tipList, remindMonthlyEvaluation } from '@/oapi/audit/newMyDraft'
  import {
    loginGetPress,
    getPressInfo,
    getFlowMessage,
  } from '@/api/contract/manage.js'
  import { loginGetNewspaper } from '@/oapi/ypns_zhgl/yxsyd.js'
  import { translateTitle } from '@/utils/i18n'
  import gzfayj from '@/views/msg/components/gzfayj'
  import gzyj from '@/views/msg/components/gzyj'
  import mxyj from '@/views/msg/components/mxyj'
  import wdcy from '@/views/msg/components/wdcy'
  import wddb from '@/views/msg/components/wddb'
  import wdpj from '@/views/msg/components/wdpj'
  import zbfnyj from '@/views/msg/components/zbfnyj'
  import zpyj from '@/views/msg/components/zpyj'
  import { mapGetters } from 'vuex'
  import SchemeInfo from '@/views/audit/rectify/components/SchemeInfo.vue'
  import { getRectificationPlanDetail } from '@/api/zgzz/index.js'
  export default {
    name: 'VabNotice',
    // eslint-disable-next-line vue/no-unused-components
    components: {
      wdcy,
      wddb,
      wdpj,
      gzfayj,
      zbfnyj,
      mxyj,
      zpyj,
      gzyj,
      Views,
      SchemeInfo,
    },
    data() {
      return {
        queryForm: {
          pageNumber: 1,
          pageSize: 5,
        },
        fqList: [],
        list: [],
        listTotal: 0,
        cyList: [],
        cyListTotal: 0,
        xxdbList: [],
        xxdbListTotal: 0,
        activeName: 'wddb',
        // 卡片图标
        tabList: [
          {
            title: '待办事宜',
            component: 'wddb',
          },
          {
            title: '消息待办',
            component: 'xxdb',
          },
          {
            title: '已办事宜',
            component: 'wdcy',
          },
          {
            title: '我发起的',
            component: 'wfqd',
          },
          {
            title: '知会事宜',
            component: 'cssy',
          },
          {
            title: '待发事宜',
            component: 'dfsy',
          },
          {
            title: '驳回事宜',
            component: 'bhsy',
          },
          {
            title: '未阅事项',
            component: 'wysx',
          },
        ],
        showDialog: false,
        // 未阅事项数据
        wyList: [],
        wyListTotal: 0,
        noticeLoadedTabs: {},
        noticePollingStarted: false,
        noticeRemindLoaded: false,
      }
    },
    computed: {
      ...mapGetters({
        theme: 'settings/theme',
      }),
      badge() {
        let total = this.listTotal
        return total === 0 ? null : total
      },
    },
    async created() {
      this.getTipList()
      // 切换模块时 Header 常驻，避免默认首页加载就拉业务/流程提醒列表。
      // 这些提醒改为用户打开通知弹层后再按需加载。
      // this.getNewspaper()
      // this.getFlowMessages()
      // this.getPressInfos()
      this.getEvents()
      // this.getZGNoticeInfo()
      this.checkDateAndCallApi()
      // 通知列表接口较多，切换模块默认首页不预加载，打开通知弹层或进入消息中心再请求。
      // this.$nextTick(() => {
      //   if (this.theme.showNotice) {
      //     this.getDaiban()
      //     this.getChuanyue()
      //     this.getFaqi()
      //     this.getshiyi()
      //     this.getdfsy()
      //     this.getbhsy()
      //     this.getXxdb()
      //     this.getWysx()
      //   }
      // })
      // 整改方案即将到期提示,一天只提醒一次，如果没有返回值就不提醒
      const lastExecutionDate = localStorage.getItem('lastZGInfoExecutionDate')
      const today = new Date().toISOString().split('T')[0] // 获取今天的日期字符串
      if (lastExecutionDate !== today) {
        const res = await getZGInfo()
        if (res.code == 1 && res.data) {
          this.getZGNoticeInfo(res.data)
          localStorage.setItem('lastZGInfoExecutionDate', today) // 更新执行日期
        }
      }
      this.$bus.$on('updateMsg', (type) => {
        this.noticeLoadedTabs = {}
        if (type === 0) {
          this.getDaiban()
        } else if (type === 1) {
          this.getChuanyue()
        } else if (type === 2) {
          this.getFaqi()
        } else if (type === 3) {
          this.getshiyi()
        } else if (type === 4) {
          this.getdfsy()
        } else if (type === 5) {
          this.getbhsy()
        } else if (type === 6) {
          this.getXxdb()
        } else if (type === 7) {
          this.getWysx()
        }
      })
      // 获取所有消息数量
      this.refreshNoticeBadge()

      // 每二分钟更新一次消息数量
      const timer3 = setInterval(() => {
        this.refreshNoticeBadge()
      }, 120000) // 120000毫秒 = 2分钟

      // 通过$once来监听定时器
      // 在beforeDestroy钩子触发时清除定时器
      this.$once('hook:beforeDestroy', () => {
        clearInterval(timer3)
      })
    },
    mounted() {
      this.$nextTick(() => {
        // 登录催办也归入通知弹层懒加载，避免每次切模块默认首页触发流程提醒接口。
      })
    },
    methods: {
      handleNoticeShow() {
        // 铃铛需要展示总数、各分类数量和列表数据；打开弹层时再集中加载，避免切模块默认首页触发。
        this.refreshNoticeBadge()
        this.loadAllNoticeTabs()
        this.startNoticePolling()
        if (!this.noticeRemindLoaded) {
          this.noticeRemindLoaded = true
          this.getNewspaper()
          if (localStorage.getItem('isFirstLogin') == 1) {
            this.getLoginGetPress()
            localStorage.setItem('isFirstLogin', false)
          }
        }
      },
      refreshNoticeBadge() {
        getAllMsgNum().then((res) => {
          if (res.code == 1) {
            this.listTotal = res.data
          }
        })
      },
      startNoticePolling() {
        if (this.noticePollingStarted) return
        this.noticePollingStarted = true
        const timer1 = setInterval(() => {
          this.getPressInfos()
        }, 300000)
        this.$once('hook:beforeDestroy', () => {
          clearInterval(timer1)
        })
        const timer2 = setInterval(() => {
          this.getFlowMessages()
        }, 300000)
        this.$once('hook:beforeDestroy', () => {
          clearInterval(timer2)
        })
      },
      loadNoticeTab(name) {
        if (this.noticeLoadedTabs[name]) return
        this.noticeLoadedTabs = {
          ...this.noticeLoadedTabs,
          [name]: true,
        }
        switch (name) {
          case 'wddb':
            this.getDaiban()
            break
          case 'wdcy':
            this.getChuanyue()
            break
          case 'wfqd':
            this.getFaqi()
            break
          case 'cssy':
            this.getshiyi()
            break
          case 'dfsy':
            this.getdfsy()
            break
          case 'bhsy':
            this.getbhsy()
            break
          case 'xxdb':
            this.getXxdb()
            break
          case 'wysx':
            this.getWysx()
            break
        }
      },
      loadAllNoticeTabs() {
        this.tabList.forEach((item) => {
          this.loadNoticeTab(item.component)
        })
      },
      async getTipList() {
        const model = localStorage.getItem('model')
        if (model != 'yqns') {
          return
        }
        const res = await tipList({ model })
        if (res.code == 1) {
          let list = res.data
          if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              let notify = await this.$notify({
                //解决消息框重叠  也可以通过加延迟解决
                title: '项目即将到期',
                message: this.$createElement('div', {}, [
                  this.$createElement(
                    'p',
                    {
                      class: 'notice-cursor',
                      on: {
                        click: () => {
                          this.$refs['edit'].showEdit(list[i], 'detail')
                        },
                      },
                    },
                    list[i].xmname
                  ),
                ]),
                dangerouslyUseHTMLString: true,
                duration: 10000,
                position: 'bottom-right',
                customClass: 'home-notice',
                offset: 100,
                onClose: () => {
                  this.closeNotification2(i + 1)
                },
              })
              this.notifications2 = notify
            }
          }
        }
      },
      //重大风险事项通知
      async getEvents() {
        const { data } = await getRemindList()
        if (data.code == 1) {
          let list = data.list
          if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              let notify = await this.$notify({
                //解决消息框重叠  也可以通过加延迟解决
                title: '重大事件待上报',
                message: list[i].riskeventcode,
                dangerouslyUseHTMLString: true,
                duration: 10000,
                position: 'bottom-right',
                customClass: 'home-notice',
                offset: 100,
                onClose: () => {
                  this.closeNotification3(i + 1)
                },
              })
              this.notifications3 = notify
            }
          }
        }
      },
      checkDateAndCallApi() {
        const today = new Date()
        const day = today.getDate()
        // 检查当前日期是否在10号到14号之间
        if (day >= 10 && day <= 14) {
          this.getRemindMonthlyEvaluation()
        }
      },
      //风险月度评估上报提醒
      async getRemindMonthlyEvaluation() {
        const { data, code } = await remindMonthlyEvaluation()
        if (code == 1) {
          let notify = await this.$notify({
            //解决消息框重叠  也可以通过加延迟解决
            title: '风险月度评估上报提醒',
            message: data.data,
            dangerouslyUseHTMLString: true,
            duration: 10000,
            position: 'bottom-right',
            customClass: 'home-notice',
            offset: 100,
            onClose: () => {
              this.closeNotification4(1)
            },
          })
          this.notifications4 = notify
        }
      },
      async getFlowMessages() {
        const res = await getFlowMessage()
        if (res.code == 1) {
          let list = res.data
          if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              let notify = await this.$notify({
                //解决消息框重叠  也可以通过加延迟解决
                title: '流程办理结果',
                message: this.$createElement('div', {}, [
                  this.$createElement(
                    'p',
                    {
                      class: 'notice-cursor',
                      on: {
                        click: () => {
                          this.showWfqd(list[i])
                        },
                      },
                    },
                    list[i].muessage
                  ),
                ]),
                dangerouslyUseHTMLString: true,
                duration: 10000,
                position: 'bottom-right',
                customClass: 'home-notice',
                offset: 100,
                onClose: () => {
                  this.closeNotification(i + 1)
                },
              })
              this.notifications = notify
            }
          }
        }
      },
      async getPressInfos() {
        // this.closeAllNotification()
        const res = await getPressInfo()
        if (res.code == 1) {
          let list = res.data.messageList
          if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              let notify = await this.$notify({
                //解决消息框重叠  也可以通过加延迟解决
                title: '催办信息',
                message: this.$createElement('div', {}, [
                  this.$createElement(
                    'p',
                    {
                      class: 'notice-cursor',
                      on: {
                        click: () => {
                          this.showWddb(list[i])
                        },
                      },
                    },
                    list[i].muessage
                  ),
                ]),
                dangerouslyUseHTMLString: true,
                duration: 10000,
                position: 'bottom-right',
                customClass: 'home-notice',
                offset: 20,
                onClose: () => {
                  this.closeNotification(i + 1)
                },
              })
              this.notifications = notify
            }
          }
        }
      },
      async getLoginGetPress() {
        const res = await loginGetPress()
        if (res.code == 1) {
          let list = res.data.messageList
          if (list.length > 0) {
            for (let i = 0; i < list.length; i++) {
              let notify = await this.$notify({
                //解决消息框重叠  也可以通过加延迟解决
                title: '催办信息',
                message: this.$createElement('div', {}, [
                  this.$createElement(
                    'p',
                    {
                      class: 'notice-cursor',
                      on: {
                        click: () => {
                          this.showWddb(list[i])
                        },
                      },
                    },
                    list[i].muessage
                  ),
                ]),
                dangerouslyUseHTMLString: true,
                duration: 10000,
                position: 'bottom-right',
                customClass: 'home-notice',
                offset: 20,
                onClose() {},
              })
              this.notifications[i + 1] = notify
            }
          }
        }
      },
      async getNewspaper() {
        const res = await loginGetNewspaper({
          belongGroup: 0,
          creator: 0,
          workUnit: 0,
        })
        if (res.code == 200 && res.data.remindMsg) {
          const msg = res.data.remindMsg
          let notify = await this.$notify({
            //解决消息框重叠  也可以通过加延迟解决
            title: '催办信息',
            message: this.$createElement('div', {}, [
              this.$createElement(
                'p',
                {
                  class: 'notice-cursor',
                  on: {
                    click: () => {
                      this.$router.push('/zhgl/sjzb')
                    },
                  },
                },
                msg
              ),
            ]),
            dangerouslyUseHTMLString: true,
            duration: 10000,
            position: 'bottom-right',
            customClass: 'home-notice',
            offset: 20,
            onClose() {},
          })
          this.notifications = notify
        }
      },
      async getZGNoticeInfo() {
        const res = await getZGInfo()
        if (res.code === 1 && res.data && res.data.length > 0) {
          let notify = await this.$notify({
            title: '整改方案到期提醒',
            message: this.$createElement(
              'div',
              {
                class: 'zg-notice-scroll',
                style: {
                  maxHeight: '400px',
                  overflowY: 'auto',
                },
              },
              [
                ...res.data.map((item) =>
                  this.$createElement(
                    'div',
                    {
                      style: {
                        display: 'flex',
                        justifyContent: 'space-between',
                        alignItems: 'center',
                        borderBottom: '1px solid #eee',
                        padding: '5px 0',
                      },
                    },
                    [
                      this.$createElement(
                        'span',
                        {
                          style: {
                            flex: 1,
                            marginRight: '10px',
                          },
                        },
                        item.distributionTitle || '整改方案即将到期'
                      ),
                      this.$createElement(
                        'el-button',
                        {
                          props: {
                            type: 'text',
                            size: 'mini',
                          },
                          on: {
                            click: () => {
                              this.showDetail(item)
                            },
                          },
                        },
                        '详情'
                      ),
                    ]
                  )
                ),
              ]
            ),
            dangerouslyUseHTMLString: true,
            duration: 30000,
            customClass: 'home-notice',
            offset: 100,
            position: 'bottom-right',
            onClose() {},
          })
          this.notifications6 = notify
        }
      },
      //打开详情
      showDetail(item) {
        this.showDialog = true
        this.$nextTick(async () => {
          const res = await getRectificationPlanDetail({ planId: item.formId })
          await this.$refs['edit'].showEdit('detail', res.data)
        })
      },
      //关闭单个通知
      closeNotification(messageId) {
        //省略部分代码
        this.notifications[messageId].close()
        delete this.notifications[messageId]
      },
      //关闭单个通知
      closeNotification2(messageId) {
        //省略部分代码
        if (this.notifications2[messageId]) {
          this.notifications2[messageId].close()
          delete this.notifications2[messageId]
        }
      },
      //关闭单个通知
      closeNotification3(messageId) {
        //省略部分代码
        if (this.notifications3[messageId]) {
          this.notifications3[messageId].close()
          delete this.notifications3[messageId]
        }
      },
      //关闭单个通知
      closeNotification4(messageId) {
        //省略部分代码
        if (this.notifications4[messageId]) {
          this.notifications4[messageId].close()
          delete this.notifications4[messageId]
        }
      },
      //关闭所有通知
      closeAllNotification() {
        for (let key in this.notifications) {
          this.notifications[key + 1].close()
          // delete this.notifications[key]
        }
      },
      translateTitle,
      showWddb(row) {
        this.$store.dispatch('work/setWbbdDetailsAction', row)
        this.$store.dispatch('work/setWddbStatesAction', true)
        this.$router.push({ name: 'wddb', query: { random: Math.random() } })
      },
      showWdcy(row) {
        this.$store.dispatch('work/setWbcyDetailsAction', row)
        this.$store.dispatch('work/setWdcyStatesAction', true)
        this.$router.push({
          name: 'wdcy',
          query: { random: Math.random() },
        })
      },
      showWfqd(row) {
        this.$store.dispatch('work/setWfqdDetailsAction', row)
        this.$store.dispatch('work/setWfqdStatesAction', true)
        this.$router.push({
          name: 'wfqd',
          query: { random: Math.random() },
        })
      },
      showDfsy(row) {
        this.$store.dispatch('work/setWfqdDetailsAction', row)
        this.$store.dispatch('work/setWfqdStatesAction', true)
        this.$router.push({
          name: 'dfsy',
          query: { random: Math.random() },
        })
      },
      showBhsy(row) {
        this.$store.dispatch('work/setWfqdDetailsAction', row)
        this.$store.dispatch('work/setWfqdStatesAction', true)
        this.$router.push({
          name: 'bhsy',
          query: { random: Math.random() },
        })
      },
      showCssy(row) {
        this.$store.dispatch('work/setCssyDetailsAction', row)
        this.$store.dispatch('work/setCssyStatesAction', true)
        this.$router.push({
          name: 'cssy',
          query: { random: Math.random() },
        })
      },
      // showWdcy()
      toMsg() {
        if (this.activeName === 'wddb') {
          this.$router.push('/msg/wddb')
        } else if (this.activeName === 'wfqd') {
          this.$router.push('/msg/wfqd')
        } else if (this.activeName === 'wdcy') {
          this.$router.push('/msg/wdcy')
        } else if (this.activeName === 'dfsy') {
          this.$router.push('/msg/dfsy')
        } else if (this.activeName === 'bhsy') {
          this.$router.push('/msg/bhsy')
        } else if (this.activeName === 'xxdb') {
          this.$router.push('/msg/wysx')
        } else if (this.activeName === 'wysx') {
          this.$router.push('/msg/wysx')
        } else {
          this.$router.push('/msg/cssy')
        }
      },
      handleClick() {
        // 通知tab首次打开才调接口，避免hover/切模块时一次性请求全部流程列表。
        this.loadNoticeTab(this.activeName)
      },
      handleClearNotice() {
        this.badge = null
        this.list = []
        this.$baseMessage('清空消息成功', 'success', 'vab-hey-message-success')
      },
      async getDaiban() {
        const {
          data: { list, totalCount },
        } = await getToDoList(this.queryForm)

        this.list = list
        // this.listTotal = totalCount
        this.tabList[0].title = '待办事宜(' + totalCount + ')'
      },
      async getChuanyue() {
        const {
          data: { list, totalCount },
        } = await my_circulation1(this.queryForm)
        this.cyList = list
        this.cyListTotal = totalCount
        this.tabList[2].title = '已办事宜(' + totalCount + ')'
      },
      async getFaqi() {
        const {
          data: { list, totalCount },
        } = await my_faqi()
        this.fqList = list
        this.fqListTotal = totalCount
        this.tabList[3].title = '我发起的(' + totalCount + ')'
      },
      async getshiyi() {
        const {
          data: { list, totalCount },
        } = await my_shiyi()
        this.syList = list
        this.syListTotal = totalCount
        this.tabList[4].title = '知会事宜(' + totalCount + ')'
      },
      async getdfsy() {
        const {
          data: { list, totalCount },
        } = await my_faqi({ status: 4 })

        this.dfList = list
        this.dfListTotal = totalCount
        this.tabList[5].title = '待发事宜(' + totalCount + ')'
      },
      async getbhsy() {
        const {
          data: { list, totalCount },
        } = await my_faqi({ status: 3 })

        this.bhList = list
        this.bhListTotal = totalCount
        this.tabList[6].title = '驳回事宜(' + totalCount + ')'
      },
      async getXxdb() {
        try {
          const {
            data: { tlist, totalRecord },
          } = await getDistributionListPage({
            isread: '0',
            moduleType: 'fxgk',
            pageNumber: 1,
            pageSize: 5,
            distributionType: '',
          })
          this.xxdbList = tlist || []
          this.xxdbListTotal = totalRecord || 0
          this.tabList[1].title = '消息待办(' + this.xxdbListTotal + ')'
        } catch (error) {
          console.error('获取消息待办数据失败:', error)
          this.xxdbList = []
          this.xxdbListTotal = 0
          this.tabList[1].title = '消息待办'
        }
      },
      showXxdb(row) {
        this.$router.push('/msg/wysx')
      },
      async getWysx() {
        try {
          const {
            data: { tlist, totalRecord },
          } = await getDistributionListPage({
            isread: '0',
            moduleType: '',
            pageNumber: 1,
            pageSize: 5,
            distributionType: '',
          })
          this.wyList = tlist || []
          this.wyListTotal = totalRecord || 0
          this.tabList[7].title = '未阅事项(' + this.wyListTotal + ')'
          console.log('🚀 ~ this.tabList:', this.tabList)
        } catch (error) {
          console.error('获取未阅事项数据失败:', error)
          this.wyList = []
          this.wyListTotal = 0
          this.tabList[7].title = '未阅事项'
        }
      },
      showWysx(row) {
        this.$router.push('/msg/wysx')
      },
    },
  }
</script>

<style lang="scss" scoped>
  ::v-deep {
    .el-tabs__active-bar {
      min-width: 28px;
    }

    .notice-popover {
      max-height: 70vh;
      display: flex;
      flex-direction: column;
    }
  }

  .notice-list {
    height: 300px;
    max-height: 40vh;
    overflow: hidden;

    ul {
      padding: 0 15px 0 0;
      margin: 0;

      li {
        display: flex;
        align-items: center;
        padding: 10px 0 10px 0;

        ::v-deep {
          .el-avatar {
            flex-shrink: 0;
            width: 50px;
            height: 50px;
            border-radius: 50%;
          }
        }

        span {
          margin-left: 10px;
        }
      }
    }
  }

  .notice-clear {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 10px 0 0 0;
    font-size: 14px;
    text-align: center;
    cursor: pointer;
    border-top: 1px solid #e8eaec;
    background-color: #fff;
    position: relative;
    z-index: 10;

    i {
      margin-right: 3px;
    }
  }
</style>

<style lang="scss">
  // 整改方案通知滚动条样式
  .zg-notice-scroll {
    &::-webkit-scrollbar {
      width: 4px;
    }

    &::-webkit-scrollbar-track {
      background: #f1f1f1;
      border-radius: 2px;
    }

    &::-webkit-scrollbar-thumb {
      background: #c1c1c1;
      border-radius: 2px;

      &:hover {
        background: #a8a8a8;
      }
    }
  }
</style>
