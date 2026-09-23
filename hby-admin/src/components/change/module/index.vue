<template>
  <div>
    <el-popover class="tank" placement="bottom" trigger="hover" width="550">
      <template #reference>
        <el-button type="warning">{{ modelName }}</el-button>
      </template>
      <div class="module-view">
        <el-row :gutter="3.5">
          <el-col v-for="item in moduleLists" :key="item.id" :span="4.7">
            <div
              v-if="item.projectType === 1"
              class="module-item"
              @click="changeModel(item.uniqueIdentification, item.projectName)"
            >
              <vab-icon
                class="menu-img"
                :icon="item.icon"
                :style="{ color: item.color }"
              />
              <p>{{ item.projectName }}</p>
            </div>
            <div
              v-else
              class="module-item"
              @click="toYmSystem(item.otherProjectRoute, item)"
            >
              <vab-icon
                class="menu-img"
                :icon="item.icon"
                :style="{ color: item.color }"
              />
              <p>{{ item.projectName }}</p>
            </div>
          </el-col>
          <!-- <el-col :span="4.8">
            <div class="module-item" @click="changeModel('mhsy')">
              <vab-icon
                class="menu-img"
                icon="apps-line"
                style="color: #5f3694"
              />
              <p>门户首页</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('znfx')">
              <vab-icon
                class="menu-img"
                icon="bar-chart-2-line"
                style="color: #ec5b56"
              />
              <p>智能分析</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('fxgk')">
              <vab-icon
                class="menu-img"
                icon="alarm-warning-line"
                style="color: #5f3694"
              />
              <p>风险管控</p>
            </div>
          </el-col>

          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('nkhg')">
              <vab-icon
                class="menu-img"
                icon="auction-line"
                style="color: #155ad4"
              />
              <p>内控管理</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('hggl')">
              <vab-icon
                class="menu-img"
                icon="pencil-ruler-2-line"
                style="color: #2c2c2b"
              />
              <p>合规管理</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('htgl')">
              <vab-icon
                class="menu-img"
                icon="archive-drawer-line"
                style="color: #c47f18"
              />
              <p>合同管理</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('fwgl')">
              <vab-icon
                class="menu-img"
                icon="scales-3-line"
                style="color: #c47f18"
              />
              <p>法务管理</p>
            </div>
          </el-col>

          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('znsj')">
              <vab-icon
                class="menu-img"
                icon="briefcase-2-line"
                style="color: #e2b03c"
              />
              <p>内部审计</p>
            </div>
          </el-col>

          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('znjk')">
              <vab-icon
                class="menu-img"
                icon="alarm-line"
                style="color: #dab253"
              />
              <p>预警平台</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('zhjd')">
              <vab-icon
                class="menu-img"
                icon="star-half-line"
                style="color: #2c2c2b"
              />

              <p>综合监督</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="toYmSystem">
              <vab-icon
                class="menu-img"
                icon="anticlockwise-line"
                style="color: #2e3fd7"
              />
              <p>业务中台</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('sjzt')">
              <vab-icon
                class="menu-img"
                icon="database-2-fill"
                style="color: #448ef7"
              />

              <p>数据中台</p>
            </div>
          </el-col>
          <el-col :span="4.8">
            <div class="module-item" @click="changeModel('xtsz')">
              <vab-icon
                class="menu-img"
                icon="settings-3-line"
                style="color: #c20d37"
              />
              <p>系统设置</p>
            </div>
          </el-col>
          -->
          <el-col v-if="moduleLists.length == 0" :span="4.8">
            <div class="module-item" @click="changeModel('xtsz', '系统设置')">
              <vab-icon
                class="menu-img"
                icon="settings-3-line"
                style="color: #c20d37"
              />
              <p>系统设置</p>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-popover>
  </div>
</template>

<script>
  import { SSOToJNFD } from '@/api/setting/system'
  import { ssfx } from '@/api/setting/themeRepertory'
  import { sjzyJump } from '@/api/setting/sjzy'
  import { getAuthListForUser } from '@/api/setting/auths'
  export default {
    name: 'ChangeModule',
    props: {
      moduleLists: {
        type: Array,
        default: () => [],
      },
    },
    data() {
      return {
        modelName: '切换模块',
        // moduleLists: [],
      }
    },
    // mounted() {
    //   this.moduleList()
    // },
    created() {
      const model = localStorage.getItem('model')
      const modelName = localStorage.getItem('modelname')
      if (!model || model === 'home') {
        this.modelName = '切换模块'
        return
      }
      this.modelName = modelName || '切换模块'
    },
    methods: {
      async changeModel(model, name = '系统设置') {
        if (!model) return
        const currentModel = localStorage.getItem('model')
        // 同一模块重复点击，直接跳首页
        if (currentModel === model) {
          if (this.$route.path !== '/') await this.$router.push('/')
          return
        }
        // 切换前先检查目标模块权限
        let authRes
        try {
          authRes = await getAuthListForUser({ moduleType: model })
        } catch (e) {
          this.$message.error('权限查询失败，请稍后重试')
          return
        }
        const rightList = authRes && authRes.data && authRes.data.rightList
        if (!rightList || rightList.length === 0) {
          this.$message.warning('您暂无该模块的访问权限，请联系管理员')
          // 重置到系统首页，不跳模块
          localStorage.setItem('model', 'home')
          localStorage.removeItem('renderMenu')
          if (this.$route.path !== '/') await this.$router.push('/')
          return
        }
        // 有一级菜单但所有 children 都为空，说明没有配置具体页面路由，跳进去必然 404
        const hasPageRoutes = rightList.some(
          (item) => item.children && item.children.length > 0
        )
        if (!hasPageRoutes) {
          this.$message.warning('该模块暂未配置可访问页面，请联系管理员')
          localStorage.setItem('model', 'home')
          localStorage.removeItem('renderMenu')
          if (this.$route.path !== '/') await this.$router.push('/')
          return
        }
        localStorage.setItem('model', model)
        localStorage.setItem('modelname', name)
        this.modelName = name
        await this.$store.dispatch('tabs/resetVisitedRoutes')
        await this.$store.dispatch('routes/setRoutes')
        await this.$router.replace('/')
      },
      async toYmSystem(url, info) {
        console.log(info, 'info')
        //智慧分析模块跳转
        if (info.uniqueIdentification == 'zhfx') {
          const { data: res } = await ssfx()
          const url = `${res.loginUrl}?loginName=${res.name}&sign=${res.sign}&timestamp=${res.time}`
          window.open(url)
          return
        }
        //审计指引模块跳转
        if (info.uniqueIdentification == 'sjzy') {
          const res = await sjzyJump()
          const url = `http://11.11.111.19:80/point/audit/document?code=${res.data.accessToken}&refreshCode=${res.data.refreshToken}&userId=1`
          window.open(url)
          return
        }
        //数据分析模块跳转
        if (info.uniqueIdentification == 'sjfx') {
          const url = `https://dqyt.dw.cnpc.com.cn/SY/de6c500ca5320650e2882ec1713ab652`
          window.open(url)
          return
        }
        const tokenXG = localStorage.getItem('admin-pro-token')
        SSOToJNFD()
          .then((result) => {
            //判断是否引迈平台
            if (info.otherNo) {
              window.open(
                `${url}/home?token=` +
                  result.data.ymToken +
                  '&majorId=' +
                  info.otherNo +
                  '&tokenXG=' +
                  tokenXG
              )
            } else {
              window.open(url)
              // window.open(
              //   `${url}/home?token=` +
              //     result.data.ymToken +
              //     '&tokenXG=' +
              //     tokenXG
              // )
            }
          })
          .catch(() => {})
      },
    },
  }
</script>

<style lang="scss" scoped>
  .module-view {
    text-align: center;
    width: 100%;
    max-height: 530px;
    overflow-x: hidden;
    margin-bottom: -10px;
    .module-item {
      background: white;
      padding: 1px 18px;
      margin: 2px 4px 15px 4px;
      box-shadow: 1px 1px 3px #888888;
      cursor: pointer;
      .menu-img {
        font-size: 48px;
      }
      p {
        width: 56px;
      }
    }
    .module-item:hover {
      box-shadow: 1px 1px 8px #888888;
    }
  }
  .tank {
    height: 450px !important;
  }
</style>
