<template>
  <el-dialog
    :close-on-click-modal="false"
    :append-to-body="true"
    :title="title"
    :visible.sync="dialogFormVisible"
    width="1000px"
    @close="close"
  >
    <el-form ref="form" label-width="120px" :model="form">
      <el-col :span="12">
        <el-form-item label="编号" prop="elementcode">
          <span>{{ form.elementcode }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制方法" prop="controlmethod">
          <span>{{ form.controlmethod }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制类型" prop="controltype">
          <span>{{ form.controltype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="控制频率" prop="controlreq">
          <span>{{ form.controlreq }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="业务描述" prop="businessdesc">
          <span>{{ form.businessdesc }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="风险描述" prop="risktype">
          <span>{{ form.risktype }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制目标" prop="material">
          <span>{{ form.material }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="控制措施" prop="controlmeasures">
          <span>{{ form.controlmeasures }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="检查方法" prop="checkmethod">
          <span>{{ form.checkmethod }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="检查程序">
          <span>{{ form.procedures }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="检查结果">
          <span>{{ form.testresult }}</span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="检查有效性">
          <span>
            {{
              form.testpointvalidity == '1'
                ? '有效'
                : form.testpointvalidity == '2'
                ? '无效'
                : '不适用'
            }}
          </span>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="备注">
          <span>{{ form.memo }}</span>
        </el-form-item>
      </el-col>
    </el-form>
    <el-col :span="24">
      <el-divider>检查资料与记录</el-divider>
    </el-col>
    <el-col :span="24">
      <el-table :data="tableData">
        <el-table-column align="center" label="附件名称" prop="attname" />
        <el-table-column align="center" label="文件大小(KB)" prop="attsize" />
        <el-table-column align="center" label="创建人" prop="uploader" />
        <el-table-column
          align="center"
          label="操作"
          show-overflow-tooltip
          width="120"
        >
          <template #default="{ row }">
            <el-button type="text" @click="handleDown(row)">下载</el-button>
            <!-- <el-button type="text" @click="handleDelete(row)" v-if="footer">
                  删除
                </el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <template #footer>
      <el-button @click="close">关 闭</el-button>
    </template>
  </el-dialog>
</template>

<script>
  import { controlTestImplDetail } from '@/api/internal/new/results'
  import { download } from '@/api/internal/score'
  export default {
    name: 'ResultsInfo',
    data() {
      return {
        title: '详细',
        dialogFormVisible: false,
        form: {
          // elementcode: '12.27yw-1',
          // controlmethod: '自动',
          // controltype: '预防性',
          // controlreq: '',
        },
        tableData: [],
      }
    },
    created() {},
    methods: {
      showEdit(row, planid) {
        this.getInfo(row, planid)
        this.dialogFormVisible = true
      },
      async getInfo(row, planid) {
        const data = await controlTestImplDetail({
          planid: planid,
          elementId: row.ELEMENTID,
        })

        this.form = { ...data.data.element, ...data.data.task }
        this.tableData = data.data.atts
      },
      close() {
        this.dialogFormVisible = false
      },
      async handleDown(row) {
        const data = await download({ attId: row.attid })
        let filename = row.attname
        let blob = new Blob([data]) //res即为blob数据，请注意自己的数据形式
        let url = window.URL.createObjectURL(blob, {
          type: 'application/vnd.ms-excel',
        })
        const link = document.createElement('a')
        link.style.display = 'none'
        link.href = url
        link.setAttribute('download', filename)
        document.documentElement.appendChild(link)
        link.click()
        document.documentElement.removeChild(link)
      },
    },
  }
</script>
<style lang="scss" scoped>
  .box_row {
    margin-bottom: 20px;
  }
  .flex {
    display: flex;
    justify-content: flex-end;
  }
</style>
