<template>
  <div class="system-log-container">
    <vab-query-form>
      <vab-query-form-left-panel :span="6">
      </vab-query-form-left-panel>
      <vab-query-form-right-panel :span="18">
        <el-button size="mini" type="primary" @click="sendModel">
          模块下发
        </el-button>
        <el-button size="mini" type="primary" @click="sendCancelModel">
          模块下发取消
        </el-button>
        <el-button size="mini" type="primary" @click="sendCompany">下发到公司</el-button>
        <el-button size="mini" type="primary" @click="sendRole">下发到角色</el-button>
        <el-button size="mini" type="primary" @click="send">下发到人员</el-button>
        <!-- <el-button size="mini" type="primary" @click="sendCancel">
          取消下发
        </el-button> -->
      </vab-query-form-right-panel>
    </vab-query-form>

    <el-table
      v-loading="listLoading"
      :data="list"
      @selection-change="handleSelectionChange"
    >
      <el-table-column align="center" type="selection" width="60" />
      <el-table-column
        align="center"
        label="编号"
        prop="agentNo"
        show-overflow-tooltip
        width="150px"
      >
        <template #default="{ row }">
          <span>{{ row.agentNo }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="分类" width="200" prop="agentType" />
      <el-table-column align="center" label="名称" width="200" prop="agentName" />
      <el-table-column align="center" label="描述" prop="describe" />
      <el-table-column
        align="center"
        label="操作"
        show-overflow-tooltip
        width="100"
      >
        <template #default="{ row }">
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      :current-page="queryForm.pageNumber"
      :layout="layout"
      :page-size="queryForm.pageSize"
      :total="total"
      @current-change="handleCurrentChange"
      @size-change="handleSizeChange"
    />

    <!-- 下发到模块 -->
    <send-model ref="sendModel" @selected="onSelectModule" />
    <!-- 取消模块下发 -->
    <send-model ref="sendCancelModel" @selected="onSelectCancelModule" />
    <!-- 下发到公司 -->
    <CompanyRoleTree ref="CompanyRoleTree" @selected="handCompanyRoleSubmit" />
    <!-- 下发到人员 -->
    <selectPerson ref="manage" @projectManage="getChildlistPro" multiple />
    <!-- 下发到角色 -->
    <RolesList ref="roles" @selected="handleSelectRoles" />
    <!-- 取消下发 -->
    <!-- <sendCancel ref="sendCancel" @fetch-data="fetchData" /> -->
  </div>
</template>

<script>
  import { agentIssued, agentIssuedList, sageOrUpdateAgent, updateIssuseModule } from '@/api/ai/index.js'
  // import sendCancel from '@/views/setting/themeRepertory/components/sendCancel'
  import CompanyRoleTree from '@/views/setting/auth/components/CompanyRoleTree'
  import selectPerson from '@/components/selectPerson.vue'
  import SendModel from '@/views/setting/themeRepertory/components/SendModel'
  import RolesList from '@/views/setting/auth/components/RolesList'

  export default {
    name: 'Theme',
    components: {
      // sendCancel,
      CompanyRoleTree,
      selectPerson,
      SendModel,
      RolesList
    },
    props: {
      themeType: {
        type: Number,
        default: 0,
      },
    },
    data() {
      return {
        list: [],
        listLoading: true,
        layout: 'total, sizes, prev, pager, next, jumper',
        total: 0,
        queryForm: {
          agentName: "",
          agentType: "",
          flagIssued: 1,
          pageNumber: 1,
          pageSize: 20,
        },
        queryTemp: [
          {
            pid: undefined,
          },
          {
            pid: undefined,
          },
          {
            pid: undefined,
          },
        ],
        multipleSelection: [],
        type: 1,
        pageIds: '',
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      handleSelectionChange(val) {
        this.multipleSelection = val
      },
      handleSizeChange(val) {
        this.queryForm.pageSize = val
        this.fetchData()
      },
      handleCurrentChange(val) {
        this.queryForm.pageNumber = val
        this.fetchData()
      },
      queryData() {
        this.queryForm.pageNumber = 1
        this.fetchData()
      },
      async fetchData() {
        this.listLoading = true
        const {
          data: { tlist, totalRecord },
        } = await agentIssuedList(this.queryForm)
        this.list = tlist
        this.total = totalRecord
        this.listLoading = false
      },
      handleDelete(row) {
        this.$baseConfirm('你确定要删除当前项吗', null, async () => {
          sageOrUpdateAgent({ id: row.id, flagIssued: 0 }).then((res) => {
            if (res.code) {
              this.$message({
                type: 'success',
                message: '删除成功!',
              })
              this.fetchData()
            }
          })
        })
      },
      sendModel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }

        this.$refs['sendModel'].show()
      },
      async onSelectModule(module) {
        const pageids = this.multipleSelection.map((i) => i.id)
        const res = await updateIssuseModule({ ids: pageids, moduleRoute: module, flagSend: 1 })
        if (res.code == 200) {
          this.$message.success('下发成功')
          this.fetchData()
        }
      },
      sendCancelModel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        const allModules = []
        this.multipleSelection.map(x => {
          if (x.moduleRoute) {
            allModules.push(x.moduleRoute.split(','))
          }
        })
        const modules = this.getIntersection(allModules)
        this.$refs['sendCancelModel'].show({ modules })
      },
      async onSelectCancelModule(module) {
        const len = this.multipleSelection.length - 1
        this.multipleSelection.map(async (x, i) => {
          const moduleRoutes = x.moduleRoute.split(',')
          let newModules = this.getUniqueElements([moduleRoutes, module.split(',')])
          const res = await updateIssuseModule({ ids: [x.id], moduleRoute: newModules.join(','), flagSend: 1 })
          if (i === len && res && res.code == 200) {
            this.$message.success('取消成功')
            this.fetchData()
          }
        })
      },
      getIntersection(arrays) {
        // 取交集
        if (arrays.length === 0) return [];

        return arrays.reduce((acc, currentArray) => {
          const currentSet = new Set(currentArray);
          return acc.filter(element => currentSet.has(element));
        }, [...arrays[0]]);
      },
      getUniqueElements(arrays) {
        // 非集
        // 统计每个元素在所有数组中出现的次数
        const countMap = new Map();
        // 遍历所有数组和元素，统计出现次数
        arrays.forEach(arr => {
            const uniqueInArray = new Set(arr); // 先处理当前数组的去重
            uniqueInArray.forEach(element => {
                countMap.set(element, (countMap.get(element) || 0) + 1);
            });
        });
        
        // 过滤出只出现一次的元素
        const result = [];
        countMap.forEach((count, element) => {
            if (count === 1) {
                result.push(element);
            }
        });
        
        return result;
      },
      sendCompany() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        this.$refs['CompanyRoleTree'].showEdit()
      },
      async handCompanyRoleSubmit(val) {
        const pageids = this.multipleSelection.map((i) => i.id)
        const res = await agentIssued({
          authorityIds: val.map((item) => item.id),
          authorityType: 1,
          ids: pageids,
          flagSend: 1
        })
        if (res.code == 200) {
          this.$message.success('下发成功')
          this.fetchData()
        }
      },
      sendRole() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }
        this.$refs.roles.show()
      },
      async handleSelectRoles(val) {
        const pageids = this.multipleSelection.map((i) => i.id)
        const res = await agentIssued({
          authorityIds: val.map((item) => item.rid),
          authorityType: 1,
          ids: pageids,
          flagSend: 1
        })
        if (res.code == 200) {
          this.$message.success('下发成功')
          this.fetchData()
        }
      },
      send() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }

        this.$refs['manage'].showEdit()
      },
      async getChildlistPro(val) {
        const pageids = this.multipleSelection.map((i) => i.id)
        const res = await agentIssued({
          authorityIds: val
            .map((item) => {
              return item.staffid
            }),
          authorityType: 3,
          ids: pageids,
          flagSend: 1
        })
        if (res.code == 200) {
          this.$message.success('下发成功')
          this.fetchData()
        }
      },
      sendCancel() {
        if (this.multipleSelection.length == 0) {
          this.$message.error('请选择')
          return
        }

        const pageids = this.multipleSelection.map((i) => i.id).join(',')

        this.$refs['sendCancel'].showEdit({
          ids: pageids,
          pid: this.queryForm.pid,
        })
      },
    },
  }
</script>
