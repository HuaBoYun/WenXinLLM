<template>
  <div>
    <el-drawer
      title="菜单导航"
      :visible.sync="showMenu"
      direction="ltr"
      size="1200px"
      :append-to-body="true"
      :style="{ margin: `${topHeight} 0 0 0` }"
      :withHeader="false"
      :modal="false"
    >
      <div class="menu-view">
        <div class="menu-module">
          <div
            style="
              margin-top: 0;
              height: 45px;
              display: flex;
              align-items: center;
            "
          >
            <el-input
              placeholder="搜索"
              v-model="searchText"
              class="borderless-input"
            >
              <i
                slot="prefix"
                class="el-input__icon el-icon-search"
                style="color: #ffb800"
              ></i>
            </el-input>
          </div>
          <div
            v-for="(item, index) in menu"
            :class="activeModule.id === item.id ? 'active' : ''"
            :key="index"
            @click="changeModule(item)"
          >
            <vab-icon
              :icon="item.icon"
              :is-custom-svg="false"
              :style="{ color: item.color }"
            />
            <label>{{ item.projectName }}</label>
          </div>
        </div>

        <div class="right" v-if="!nomenu">
          <div class="menu-catalog">
            <div
              v-for="(item, index) in activeModule.menu"
              :class="activeCatalog.id === item.id ? 'active' : ''"
              :key="index"
              @click="changeCatalog(item)"
            >
              <!-- <vab-icon
                :icon="item.icon"
                :is-custom-svg="true"
                :style="{ color: item.color }"
              /> -->
              <label>{{ item.name }}</label>
            </div>
          </div>

          <div class="menu-page">
            <div v-for="(item, index) in renderData" :key="index">
              <h3 :class="activeCatalog.id === item.id ? 'titleActive' : ''">
                {{ item.name }}
              </h3>
              <div>
                <span
                  v-for="(res, i) in item.children"
                  :key="i"
                  @click="toPage(res)"
                  :class="renderPath.id === res.id ? 'active' : ''"
                >
                  {{ res.name }}
                </span>
              </div>
            </div>
          </div>
          <!-- <div class="menu-page">
            <div
              v-for="(item, index) in activeCatalog.children"
              :key="index"
              @click="toPage(item)"
            >
              <vab-icon
                :icon="item.icon"
                :is-custom-svg="true"
                :style="{ color: item.color }"
              />
              <label>{{ item.name }}</label>
            </div>
          </div> -->
        </div>
        <div class="noRight" v-if="nomenu">
          <div class="mylove">
            <div class="mylove_title">我的收藏</div>
            <div class="mylove_content">
              <div
                class="mylove_item"
                v-for="item in renderData[5].children"
                :key="item.id"
                @click="toMyPage(item)"
              >
                {{ item.name }}
              </div>
            </div>
          </div>

          <div class="near">
            <div class="near_title">最近访问</div>
            <div class="near_content">
              <div
                class="near_item"
                v-for="item in renderData[6].children"
                :key="item.id"
                @click="toMyPage(item)"
              >
                <div class="near_item_name">
                  <div>
                    {{ item.name }}
                  </div>
                  <img :src="Black" alt="" class="star" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
  import { getAuthListForUser } from '@/api/setting/auths'
  import Black from '@/assets/blackStar.png'
  import Yellow from '@/assets/yellowStar.png'
  export default {
    name: 'AllMenu',
    data() {
      return {
        showMenu: false,
        menu: [],
        activeModule: {},
        activeCatalog: {},
        activePage: {},
        renderPath: {}, //当前页面的信息
        model: '',
        topHeight: 0,
        searchText: '',
        renderData: [], //最右侧整体数据
        nomenu: false,
        Black,
        Yellow,
      }
    },
    mounted() {
      //适配不同电脑的顶栏高度
      const element = document.getElementsByClassName('custom-header')
      const height = element[0].clientHeight
      this.topHeight = height + 'px'
    },

    methods: {
      show() {
        this.model = localStorage.getItem('model')
        this.menu = JSON.parse(localStorage.getItem('allMenu')) || []
        this.showMenu = true
        for (let i = 0; i < this.menu.length; i++) {
          if (this.menu[i].uniqueIdentification === this.model) {
            this.activeModule = this.menu[i]
          }
        }
        //初始化最右边的数据
        let info = this.activeModule.menu
        info &&
          info.forEach((res1, i) => {
            res1.children.forEach((res2, j) => {
              info[i].children[j].ppath = res1.path
            })
          })
        this.renderData = info

        if (this.$route.path) {
          const path = this.$route.path.split('/')
          const first = path[1]
          const second = path[2]
          //初始回填页面模块信息
          const arr = this.activeModule.menu.filter((res) => {
            return res.path == first
          })
          this.activeCatalog = arr[0]
          //初始回填页面模块下页面的信息
          const arr1 = arr[0].children.filter((res1) => {
            return res1.path == second
          })
          this.renderPath = arr1[0]
        }
      },
      changeModule(item) {
        if (item.projectName === '常用') {
          this.nomenu = true
          this.activeModule = item
          return
        } else {
          this.nomenu = false
        }
        this.activeModule = item
        //点击菜单实时调取菜单接口渲染
        getAuthListForUser({ moduleType: item.uniqueIdentification }).then(
          (res) => {
            // item.menu = res.data.rightList
            let info = res.data.rightList
            //处理数据
            info.forEach((res1, i) => {
              res1.children.forEach((res2, j) => {
                info[i].children[j].ppath = res1.path
              })
            })
            this.renderData = info
            // this.activeModule.menu = info
            //请求到的数据存起来，用于再次展开菜单
            const index = this.menu.findIndex(
              (element) =>
                element.uniqueIdentification == item.uniqueIdentification
            )
            this.menu[index].menu = res.data.rightList
            localStorage.setItem('allMenu', JSON.stringify(this.menu))
          }
        )
      },
      changeCatalog(item) {
        this.activeCatalog = item
      },
      toPage(item) {
        let path = '/' + item.ppath + '/' + item.path
        if (this.model !== this.activeModule.uniqueIdentification) {
          localStorage.setItem('model', this.activeModule.uniqueIdentification)
          localStorage.setItem('modelname', this.activeModule.projectName)
          window.location.href = '/'
          localStorage.setItem('path', path)
        } else {
          console.log('未切换模块')
          this.$router.push(path)
        }
        this.showMenu = false
      },
      toMyPage(item) {
        console.log(item, 'item')
        console.log(this.activeModule, 'this.activeModule')
        return
        let path = '/' + item.ppath + '/' + item.path
        if (this.model !== item.moduletype) {
          localStorage.setItem('model', item.moduletype)
          localStorage.setItem('modelname', item.projectName)
          window.location.href = '/'
          localStorage.setItem('path', path)
        } else {
          console.log('未切换模块')
          this.$router.push(path)
        }
        this.showMenu = false
      },
      close() {
        this.showMenu = false
      },
    },
  }
</script>

<style lang="scss" scoped>
  .menu-view {
    display: flex;
    background: #f2f2f2;
    .menu-module {
      width: 270px;
      padding: 3px;
      max-height: calc(100vh - 60px);
      overflow-y: scroll;
      margin-top: 5px;
      // 添加滚动条样式
      &::-webkit-scrollbar {
        width: 6px;
      }
      &::-webkit-scrollbar-thumb {
        background: #e7ecf2;
        border-radius: 3px;

        &:hover {
          background: #c0c4cc; // 悬浮时变成更深的颜色
        }
      }
      &::-webkit-scrollbar-track {
        background: #f2f2f2;
      }
      div {
        position: relative;
      }
      > div:hover {
        /* background: gainsboro; */
        cursor: pointer;
        label {
          color: $base-color-blue;
          cursor: pointer;
        }
      }
      .active {
        /* background: gainsboro; */
        &::before {
          content: '';
          height: 42px;
          width: 3px;
          top: 50%;
          left: 0;
          transform: translateY(-50%);
          border-radius: 50px;
          overflow: hidden;
          position: absolute;
          background-color: $base-color-blue;
        }
        label {
          color: $base-color-blue;
        }
      }
      > div {
        margin-top: 7px;
        padding: 11px;
        background: white;
        border-radius: 5px;
        i {
          font-size: 20px;
        }
        label {
          font-size: 14px;
          margin-left: 5px;
        }
      }
      .borderless-input {
        :deep(.el-input__inner) {
          border: none;
          border-radius: 4px;

          &:focus {
            box-shadow: none;
          }

          &::placeholder {
            font-size: 14px;
          }
        }
      }
    }
    .right {
      display: flex;
      // background: gainsboro;
      flex: 1;
      padding: 3px 0 0 0;
      margin-top: 5px;

      .menu-catalog {
        width: 190px;
        padding: 0px 10px 10px 0;
        background: white;
        border-radius: 8px 0 0 8px;
        max-height: calc(100vh - 60px);
        overflow-y: scroll;
        cursor: pointer;
        // 添加滚动条样式
        &::-webkit-scrollbar {
          display: none;
        }
        &::-webkit-scrollbar-thumb {
          background: #e7ecf2;
          border-radius: 3px;

          &:hover {
            background: #c0c4cc; // 悬浮时变成更深的颜色
          }
        }
        &::-webkit-scrollbar-track {
          background: #f2f2f2;
        }
        div {
          position: relative;
          cursor: pointer;
        }
        > div:hover {
          background: #f6f6f6;
          cursor: pointer;
          label {
            color: $base-color-blue;
            cursor: pointer;
          }
        }
        .active {
          background: #f6f6f6;
          &::before {
            content: '';
            height: 25px;
            width: 3px;
            top: 50%;
            left: 0;
            transform: translateY(-50%);
            border-radius: 50px;
            overflow: hidden;
            position: absolute;
            background-color: $base-color-blue;
          }
          label {
            color: $base-color-blue;
          }
        }
        > div {
          margin-top: 10px;
          padding: 10px;
          background: white;
          border-radius: 5px;
          i {
            font-size: 20px;
          }
          label {
            font-size: 14px;
            margin-left: 5px;
          }
        }
      }

      .menu-page {
        flex: 1;
        padding: 0px 0 10px 10px;
        background: white;
        border-left: 1px solid gainsboro;
        max-height: calc(100vh - 60px);
        overflow-y: scroll;
        h3 {
          font-size: 14px;
          position: relative;
          margin-bottom: 5px;
        }
        // 添加滚动条样式
        &::-webkit-scrollbar {
          width: 6px;
          cursor: pointer;
        }
        &::-webkit-scrollbar-thumb {
          background: #e7ecf2;
          border-radius: 3px;
          cursor: pointer;
          &:hover {
            background: #c0c4cc; // 悬浮时变成更深的颜色
            cursor: pointer;
          }
        }
        &::-webkit-scrollbar-track {
          background: #f2f2f2;
        }
        > div:hover {
          /* background: gainsboro; */
          label {
            color: $base-color-blue;
          }
        }
        .active {
          color: $base-color-blue;
          /* background: gainsboro; */
          label {
            color: $base-color-blue;
          }
        }
        .titleActive {
          color: $base-color-blue;
          &::before {
            content: '';
            height: 15px;
            width: 3px;
            top: 50%;
            left: -10px;
            transform: translateY(-50%);
            border-radius: 50px;
            overflow: hidden;
            position: absolute;
            background-color: $base-color-blue;
          }
          /* background: gainsboro; */
          label {
            color: $base-color-blue;
          }
        }
        > div {
          margin-top: 10px;
          padding: 10px;
          background: white;
          border-radius: 5px;
          i {
            font-size: 20px;
          }
          span {
            font-size: 13px;
            margin-left: 2px;
            width: 33%;
            display: inline-block;
            padding: 7px 0;
            &:hover {
              cursor: pointer;
            }
          }
        }
      }
    }
    .noRight {
      background-color: #fff;
      width: 100%;
      padding: 20px;
      .mylove {
        .mylove_content {
          display: flex;
          flex-wrap: wrap;
        }
        .mylove_title {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 20px;
        }
        .mylove_item {
          width: 33.3%;
          cursor: pointer;
          margin-bottom: 15px;
        }
      }
      .near {
        margin-top: 70px;
        .near_content {
          display: flex;
          flex-wrap: wrap;
        }
        .near_title {
          font-size: 18px;
          font-weight: 600;
          margin-bottom: 20px;
        }
        .near_item {
          width: 33.3%;
          cursor: pointer;
          margin-bottom: 15px;
          .near_item_name {
            width: 50%;
            padding: 3px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            &:hover {
              .star {
                display: block;
              }
            }
          }
        }
      }
    }
    .star {
      width: 15px;
      height: 15px;
      display: none;
    }
  }
</style>
