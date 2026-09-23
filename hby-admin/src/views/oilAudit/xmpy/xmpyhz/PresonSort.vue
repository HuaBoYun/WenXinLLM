<template>
  <el-dialog
    :visible.sync="dialogVisible"
    :modal="modal"
    width="800px"
    :close-on-click-modal="false"
  >
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <vab-query-form>
            <vab-query-form-right-panel :span="24">
              <el-button @click="dialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save">确 定</el-button>
            </vab-query-form-right-panel>
          </vab-query-form>
          <el-table v-loading="listLoading" :data="list" style="width: 100%">
            <el-table-column
              label="用户真实名"
              prop="realname"
            ></el-table-column>
            <el-table-column
              align="center"
              prop="sort"
              label="排序"
              width="100"
            >
              <template slot-scope="scope">
                <el-input
                  @input="handleInput(scope.$index, scope.row)"
                  v-model="scope.row.userGrade"
                  size="mini"
                  style="width: 90%"
                  type="number"
                  :disabled="scope.row.disabled"
                />
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
  </el-dialog>
</template>
<script>
  import {
    findOrganizationByTreeAllss,
    selectPerson,
  } from '@/api/audit/project'
  import DeepTree from '@/components/DepTree.vue'

  export default {
    props: {
      modal: {
        type: Boolean,
        default: false,
      },
      multiple: {
        type: Boolean,
        default: true,
      },
    },
    components: { DeepTree },
    data() {
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      const curOrgId = userInfo.currentOrg.orgid
      return {
        listLoading: false,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        curOrgId,
        dialogVisible: false,
        list: [],
        dataTree: [],
        multipleSelection: [],

        queryForm: {
          orgid: undefined,
          pageNumber: 1,
          pageSize: 20,
          ralename: undefined,
        },
        current: undefined,
        reviewType: '',
        select: [],
      }
    },
    methods: {
      showEdit(e) {
        console.log(e, 'e')
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        this.dialogVisible = true
        const info = e.map((res) => {
          return {
            ...res,
            userGrade: res.userGrade || '',
            realname: res.realname || res.userName,
            disabled: userInfo.realname != res.realname,
          }
        })
        this.list = info
      },

      /**
       * @description: 保存表单
       * @return {*}
       */
      save() {
        // if (this.select.length == 0) {
        //   this.$baseMessage('请选择人员！', 'error', 'vab-hey-message-error')
        //   return
        // }
        const info = this.list.map((res) => {
          return { ...res, userId: res.staffid || res.userId }
        })
        this.$emit('selected', info)
        this.dialogVisible = false
      },
      handleInput(a, b) {
        //a是索引
        this.list[a] = b
      },
    },
  }
</script>
<style scoped lang="scss">
  .lr-layout {
    display: flex;
  }

  .lr-layout > .left {
    width: 350px;
    border-right: 1px solid ghostwhite;
    margin-right: 100px;
    padding-right: 10px;
  }

  .lr-layout > .right {
    flex: 1;
  }
</style>
