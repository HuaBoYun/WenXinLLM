<template>
  <div class="container">
    <el-dialog
      :close-on-click-modal="false"
      :title="title"
      :visible.sync="dialogFormVisible"
      v-if="dialogFormVisible"
    >
      <el-form :model="form">
        <el-form-item label="分组名" label-width="80px" prop="groupname">
          <el-input v-model="form.groupname" autocomplete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="saveTab">
          确 定
        </el-button>
      </div>
    </el-dialog>
    <div class="tabBackColor" @click="selectOrog({ fxjktype: type })">
      <!-- <i class="el-icon-s-custom"></i> -->
      <img src="../images/concat.png" alt="" />
      <div class="boxRight">
        <div>全部</div>
        <div>{{ total }}</div>
      </div>
    </div>
    <i
      class="el-icon-arrow-left"
      @click="move('left')"
      @mousedown="moveDown('left')"
      @mouseup="moveUp"
    ></i>

    <div class="content" ref="content">
      <div
        class="box1"
        v-for="(item, index) in tabList"
        :key="index"
        ref="box"
        :class="{ active: isActive === index }"
      >
        <div class="boxContent" @click="selectOrog(item, index)">
          <i class="el-icon-close deltee-icon" @click="deleteOrog(item)"></i>
          <img src="../images/team.png" alt="" />
          <div class="boxRight">
            <div>{{ item.teamname }}</div>
            <div>{{ item.companyCount }}</div>
          </div>
        </div>
        <!-- <div class="line"></div> -->
      </div>
    </div>
    <i
      class="el-icon-arrow-right"
      @click="move('right')"
      @mousedown="moveDown('right')"
      @mouseup="moveUp"
    ></i>
    <div class="tabAdd" @click="openModal">
      <!-- <i class="el-icon-circle-plus-outline"></i> -->
      <img src="../images/add.png" alt="" />
      <div>新增分组</div>
    </div>
  </div>
</template>

<script>
  import {
    getTabListData,
    addOrganization,
    deleteGroupItem,
  } from '@/api/risk/monitor'
  export default {
    name: 'SupplierTabs',
    props: ['saveTeamId', 'type'],
    data() {
      return {
        saveLoading: false,
        total: 0,
        tabList: [],
        title: '新增',
        form: {
          groupname: undefined,
        },
        dialogFormVisible: false,
        dialogVisible: false,
        value: '',
        step: 49,
        scrollLeft: 0,
        len: 0,
        isActive: undefined,
      }
    },
    created() {
      this.reloadTabData()
      this.$bus.$on('reloadTopbar', () => {
        this.reloadTabData()
      })
    },

    mounted() {
      new Swiper('.swiper-container', {
        //direction: 'vertical', // 垂直切换选项
        //mousewheel: true, //滚轮
        autoplay: {
          //自动开始
          delay: 2500, //时间间隔
          disableOnInteraction: false, //*手动操作轮播图后不会暂停*
        },
        loop: true, // 循环模式选项

        // 如果需要分页器
        pagination: {
          el: '.swiper-pagination',
          clickable: true, // 分页器可以点击
        },

        // 如果需要前进后退按钮
        navigation: {
          nextEl: '.swiper-button-next',
          prevEl: '.swiper-button-prev',
        },

        // 如果需要滚动条
        scrollbar: {
          el: '.swiper-scrollbar',
        },
      })
    },
    methods: {
      // 删除分组
      deleteOrog(e) {
        this.$confirm('删除分组会同时删除分组下公司，确认要删除吗？', '提示', {
          type: 'warning',
        }).then(async () => {
          const { code } = await deleteGroupItem({
            teamid: e.teamid,
          })
          if (code == 1) {
            this.$baseMessage('删除成功', 'success', 'vab-hey-message-success')
            this.reloadTabData()
            this.selectOrog({ fxjktype: this.type })
          } else {
            this.$baseMessage('删除失败', 'error', 'vab-hey-message-error')
          }
        })
      },
      reloadTabData() {
        getTabListData({
          fxjktype: this.type,
        }).then((res) => {
          this.tabList = res.data.teams
          this.total = res.data.count
        })
        this.$emit('fetch-data')
      },
      move(e) {
        let content = this.$refs.content

        if (content.scrollWidth > content.clientWidth) {
          if (e == 'left' && this.scrollLeft > 0) {
            // this.step++
            this.scrollLeft -= this.step
          } else if (
            e == 'right' &&
            this.scrollLeft + content.clientWidth < content.scrollWidth
          ) {
            console.log('right')
            // this.step++
            this.scrollLeft += this.step
          }
        }
      },
      moveUp() {
        clearInterval(this.timer)
      },
      moveDown(e) {
        const content = this.$refs.content
        if (content.scrollWidth > content.clientWidth) {
          this.timer = setInterval(() => {
            if (e == 'left' && this.scrollLeft > 0) {
              this.scrollLeft -= this.step
            } else if (
              e == 'right' &&
              this.scrollLeft + content.clientWidth < content.scrollWidth
            ) {
              this.scrollLeft += this.step
            }
            content.scrollTo(this.scrollLeft, 0)
          }, 100)
        }
      },

      openModal(row) {
        if (row && row.teamid) {
          row.teamid = String(row.teamid)
          this.title = '编辑分组'
          this.form = row
          this.$set(this.form, 'groupname', row.title)
        } else {
          this.title = '新增分组'
          this.form = {
            groupname: undefined,
            title: undefined,
          }
        }
        this.dialogFormVisible = true
      },
      saveTab() {
        this.saveLoading = true
        addOrganization({ ...this.form, fxjktype: this.type })
          .then(() => {
            if (this.form.teamid) {
              this.$message.success('编辑成功')
            } else {
              this.$message.success('新增成功')
            }
            this.saveLoading = false
            this.reloadTabData()
            this.dialogFormVisible = false
          })
          .finally(() => {
            this.saveLoading = false
          })
      },
      close() {
        this.dialogVisible = false
      },
      selectOrog(info, index) {
        console.log(info, 'info')
        this.isActive = index
        this.saveTeamId(info)
      },
    },
  }
</script>

<style lang="scss" scoped>
  .container {
    width: 100%;
    display: flex;
    align-items: center;
    // justify-content: space-between;
    height: 70px;
    margin-bottom: 20px;
    .el-icon-s-custom {
      font-size: 36px;
      color: #448ef7;
    }
    .tabBackColor {
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      background-color: #f0f2f5;
      height: 100%;
      min-width: 180px;
      .boxRight {
        margin-left: 10px;
      }
    }
    .tabAdd {
      height: 100%;
      min-width: 80px;
      background-color: #448ef7;
      color: #fff;
      text-align: center;
      box-shadow: 0px 0px 10px 5px rgba(0, 0, 0, 0.3);
      padding-top: 5px;
      .el-icon-circle-plus-outline {
        font-size: 36px;
        color: #fff;
      }
    }
    .content::-webkit-scrollbar {
      display: none; /* chrome 隐藏滚动条 */
    }
    .content {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      // overflow: hidden;
      overflow-x: auto;
      scrollbar-width: none;
      .active {
        color: red;
      }
      .box1 {
        display: flex;
        align-items: center;
        height: 100%;
        margin-right: 20px;
        /* color: red; */
        :hover {
          cursor: pointer; /*鼠标变小手*/
          /* background-color: red; */
        }
        .boxContent {
          left: 0;
          display: flex;
          align-items: center;
          justify-content: center;
          width: 180px;
          height: 100%;
          background-color: #f0f2f5;
          position: relative;
          .deltee-icon {
            position: absolute;
            right: 0;
            top: 0;
            font-size: 18px;
            z-index: 999;
            color: #578cf0;
            display: none;
          }
          .boxRight {
            margin-left: 10px;
          }
        }
        .boxContent:hover {
          .deltee-icon {
            display: block;
          }
        }
        .line {
          height: 100%;
          width: 25px;
          background: #fafafa;
          :hover {
            cursor: default;
          }
          // z-index: 100;
        }
      }
    }
    .el-icon-arrow-left,
    .el-icon-arrow-right {
      height: 100%;
      width: 70px;

      line-height: 70px;
      text-align: center;
      font-size: 36px;
      font-weight: bold;
      color: #448ef7;
    }
    img {
      width: 40px;
      height: 40px;
    }
  }
</style>
