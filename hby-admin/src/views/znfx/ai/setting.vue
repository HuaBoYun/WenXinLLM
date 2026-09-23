<template>
  <div class="table_content">
    <el-dialog :visible.sync="dialogVisible_setting" :before-close="close" width="1000px">
      <el-menu slot="title" :default-active="activeIndex" class="el-title-menu" mode="horizontal" @select="handleSelect">
        <el-menu-item index="1">数据配置</el-menu-item>
        <el-menu-item index="2">应用配置</el-menu-item>
      </el-menu>
      <el-row class="content">
        <el-col :span="4" class="left">
          <template v-if="activeIndex == 1">
            <div><el-button :type="leftTab == 1 ? 'primary' : ''" class="btnStyle" @click="changeLeftTab(1)">表格管理</el-button></div>
            <div><el-button :type="leftTab == 2 ? 'primary' : ''" class="btnStyle" @click="changeLeftTab(2)">知识</el-button></div>
          </template>
          <template v-else>
            <div><el-button type="primary" class="btnStyle">对话管理</el-button></div>
          </template>
        </el-col>
        <template v-if="activeIndex == 1">
          <el-col :span="20" class="right" v-if="leftTab == 1">
            <el-radio-group size="small" v-model="tabPosition" @input="radioChange" style="margin-bottom: 30px;">
              <el-radio-button v-for="(i,index) in tableList" :key="index" :label="index">{{ i.customTable }}</el-radio-button>
            </el-radio-group>
            <el-form :model="tableList[tabPosition]" :rules="rules" ref="ruleForm" label-width="60px" style="width: 80%">
              <el-form-item label="表名称" prop="customTable">
                <el-input v-model="tableList[tabPosition].customTable" maxlength="30" show-word-limit disabled></el-input>
                <p class="inputTips">表名可使用中文、英文、数字或下划线（_）。名称不超过30个字符。</p>
              </el-form-item>
              <el-form-item label="表描述" prop="description">
                <el-input v-model="tableList[tabPosition].description" maxlength="50" show-word-limit></el-input>
                <p class="inputTips">表描述可使用中文、英文、数字或常见字符。描述不超过50个字符。</p>
              </el-form-item>
            </el-form>
            <el-table v-if="leftTab == 1" ref="mainTable" :data="tableList[tabPosition].settings" max-height="430px" size="small" style="width: 100%">
              <el-table-column prop="columnName" label="列名"></el-table-column>
              <el-table-column label="" prop="fieldType">
                <template slot="header" slot-scope="scope">
                  <div>数据类型<el-tooltip class="item" effect="dark" content="数据类型将决定系统对数据的理解方式" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip></div>
                </template>
              </el-table-column>
              <el-table-column label="示例数据" prop="demo"></el-table-column>
              <el-table-column label="">
                <template slot="header" slot-scope="scope">
                  <div>是否建立索引<el-tooltip class="item" effect="dark" content="系统智能判断， 请谨慎调整" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip></div>
                </template>
                <template #default="scope">
                  <el-switch v-model="scope.row.indexed" />
                </template>
              </el-table-column>
              <el-table-column label="">
                <template slot="header" slot-scope="scope">
                  <div>列描述<el-tooltip class="item" effect="dark" content="更详尽精准的描述将有利于大模型更好地理解表格，给出更优质答案" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip></div>
                </template>
                <template #default="scope">
                  <el-input v-model="scope.row.columnComment" placeholder="请输入内容"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="">
                <template slot="header" slot-scope="scope">
                  <div>列关联<el-tooltip class="item" effect="dark" content="高级功能，请谨慎调整。如需使用请参考操作手册。" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip></div>
                </template>
                <template #default="scope">
                  <el-select v-model="scope.row.parentColumn" placeholder="请选择">
                    <el-option
                      v-for="item in tableList[tabPosition].settings"
                      :key="item.columnName"
                      :label="item.columnName"
                      :value="item.id"
                      :disabled="item.columnName == scope.row.columnName">
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
          <el-col :span="20" class="right" v-else>
            <el-button type="primary" plain icon="el-icon-plus" @click="addKnow">新增</el-button>
            <el-table v-if="leftTab == 2" ref="knowTable" :data="knowledgeList" max-height="430px" size="small" v-loading="knowTableLoading" style="width: 100%;margin-top:10px;">
              <el-table-column prop="knowledgeKey" label="自定义知识">
                <template #default="scope">
                  <el-input v-if="scope.row.isChange" v-model="scope.row.knowledgeKey" placeholder="请输入"></el-input>
                  <div v-else>{{ scope.row.knowledgeKey }}</div>
                </template>
              </el-table-column>
              <el-table-column label="">
                <template slot="header" slot-scope="scope">
                  <div>对大模型解释这条知识<el-tooltip class="item" effect="dark" content="具体使用方式参考操作手册" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip></div>
                </template>
                <template #default="scope">
                  <el-input v-if="scope.row.isChange" v-model="scope.row.knowledgeValue" placeholder="请输入"></el-input>
                  <div v-else>{{ scope.row.knowledgeValue }}</div>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <div v-if="!scope.row.isChange">
                    <el-link :type="!scope.row.isEdit ? 'info' : 'primary'" size="small" :underline="false" :disabled="!scope.row.isEdit" @click="edit(scope.row)" style="margin-right: 20px">编辑</el-link>
                    <el-link type="primary" size="small" :underline="false" @click="omit(scope.row)">删除</el-link>
                  </div>
                  <div v-else>
                    <el-link type="primary" :underline="false" size="small" @click="preserve(scope.row)" style="margin-right: 20px">保存</el-link>
                    <el-link type="primary" :underline="false" size="small" @click="cancel(scope.row)">取消</el-link>
                  </div>
                </template>
              </el-table-column>
            </el-table>
          </el-col>
        </template>
        <template v-else>
          <el-col :span="20" class="right">
            <el-form label-width="100px" style="width: 80%">
              <el-form-item label="当连续提问时:" prop="customTable">
                <el-radio-group v-model="applicationConfig.continuousQuestioning" @change="appSetting">
                  <div class="radioStyle"><el-radio label="1">自动联系上文进行回答（测试功能，请谨慎使用）</el-radio></div>
                  <div class="radioStyle"><el-radio label="2">仅基于当前提问进行回答</el-radio></div>
                </el-radio-group>
              </el-form-item>
            </el-form>
          </el-col>
        </template>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <template v-if="activeIndex == 1 && leftTab == 1">
          <el-button @click="dialogVisible_setting = false">取 消</el-button>
          <el-button type="primary" @click="updateTable()">确 定</el-button>
        </template>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getTableList, getTableData, getKnowledgeList, getApplicationConfig, addKnowledge, deleteKnowledge, updateKnowledge, saveApplicationConfig, updateTableColumns } from '@/api/ai/index.js'
export default {
  data(){
    return{
      dialogVisible_setting:false,
      activeIndex:"1",
      tabPosition:'',
      tableData:[],
      column:[],
      ruleForm: {
        name: '订单明细'
      },
      rules: {},
      tableList: [],
      knowledgeList: [],
      leftTab: 1,
      applicationConfig: {},
      knowTableLoading: false,
      id: ''
    }
  },
  methods:{
    handleSelect(key, keyPath) {
      if(key == 2){
        getApplicationConfig().then(res => {
          this.applicationConfig = res.data
        })
      }
      this.activeIndex = key
    },
    radioChange(value){
      this.tabPosition = value
      this.ruleForm = this.tableList[value]
      this.$nextTick(() => {
        this.$refs['mainTable'].doLayout();
      })
    },
    open(id){
      this.leftTab = 1
      this.activeIndex = '1'
      this.id = id
      this.getInfo()
    },
    getInfo() {
      getTableList({batchId: this.id}).then(res => {
        this.tableList = res.data
        this.radioChange(0)
        this.dialogVisible_setting = true
      })
    },
    close(){
      this.dialogVisible_setting = false
    },
    changeLeftTab(val){
      this.leftTab = val
      if(val == 1){
        this.getInfo()
      }else{
        this.getKnowInfo()
      }
    },
    edit(row) {
      row.isChange = true
      this.knowledgeList = this.knowledgeList.map(i=>{ return { ...i, isEdit: false }})
    },
    cancel(row) {
      if(!row.id){
        this.knowledgeList = this.knowledgeList.slice(0, this.knowledgeList.length - 1);
      }
      row.isChange = false
      this.knowledgeList = this.knowledgeList.map(i=>{ return { ...i, isEdit: true }})
      console.log(this.knowledgeList)
    },
    addKnow() {
      if(this.knowledgeList.some(item => item.isChange === true)) {
        this.$message.error('请先将当前行填写完整!');
        return false;
      }
      this.knowledgeList.push({ knowledgeKey: '', knowledgeValue: '', isChange: true})
      this.knowledgeList = this.knowledgeList.map(i=>{ return { ...i, isEdit: false }})
      this.$nextTick(() => {
        this.$refs['knowTable'].doLayout();
      })
    },
    omit(row) {
      this.$confirm('请确定是否要删除?', '删除通知', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        beforeClose: (action, instance, done) => {
          if (action === 'confirm') {
            deleteKnowledge({knowledgeId: row.id}).then(res => {
              if(res.code == 1){
                this.getKnowInfo()
                this.$message.success('删除成功');
                done();
              }
            })
          } else {
            done();
          }
        }
      }).then(() => {}).catch(() => {
        this.$message.info('已取消');          
      });
    },
    preserve(row) {
      if(!row.knowledgeKey) {
        this.$message.error('请输入一条知识');
        return false;
      }
      if(!row.knowledgeValue) {
        this.$message.error('请输入对知识的解释');
        return false;
      }
      if(row.id){
        updateKnowledge({ id: row.id, knowledgeKey: row.knowledgeKey, knowledgeValue: row.knowledgeValue }).then(res =>{
          if(res.code == 1){
            this.$message.success('修改成功');
            this.getKnowInfo()
          }
        })
      }else{
        addKnowledge({ knowledgeKey: row.knowledgeKey, knowledgeValue: row.knowledgeValue }).then(res =>{
          if(res.code == 1){
            this.$message.success('新增成功');
            this.getKnowInfo()
          }
        })
      }
    },
    getKnowInfo() {
      this.knowTableLoading = true
      getKnowledgeList().then(res => {
        this.knowledgeList = res.data.map(i=>{ return { ...i, isChange: false, isEdit: true }})
        this.knowTableLoading = false
      })
    },
    appSetting(val) {
      saveApplicationConfig({continuousQuestioning:val}).then(res => {
        if(res.code == 1){
            this.$message.success('保存成功');
            this.getKnowInfo()
        }
      })
    },
    updateTable() {
      this.tableList[this.tabPosition].settings = this.tableList[this.tabPosition].settings.map(obj => {  
          let newObj = {...obj};  
          delete newObj.createTime;  
          return newObj;  
      });
      updateTableColumns([this.tableList[this.tabPosition]]).then(res => {
        if(res.code == 1){
            this.$message.success('保存成功');
        }
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-title-menu{
  margin-bottom: 16px;
}
.el-menu.el-menu--horizontal{
  border-bottom: 1px solid transparent!important;
}
.el-menu--horizontal>.el-menu-item{
  height: 36px;
  line-height: 36px;
}
.pagination_style{
  margin-top: 16px;
  text-align: right;
}
.el-table__header{
    background: rgb(247, 247, 249);
    color: rgb(92, 95, 102);
    font-weight: 400;
}
.el-table{
  color: rgb(21, 27, 38);
  font-size: 12px;
}
.el-radio-group .is-active{
  //border: 1px solid #1677ff!important;
}
.inputTips {
  color: rgb(132, 134, 140);
    font-size: 12px;
    margin:0;
}


:deep(.el-dialog__body) {
  padding-top:0;
  padding-bottom:0;
}
:deep(.el-radio-group) {
  margin-bottom: 10px!important;
}
:deep(.el-menu) {
  border-right:0;
}

.right {
  border-left: solid 1px #e6e6e6;
  padding: 20px;
  min-height: 450px;
}

.left {
  padding-top: 15px;
}

.content {
  border: solid 1px #e6e6e6;
}

.btnStyle {
  width:100%;
  border:none;
  height: 40px;
  text-align: left;
}

.radioStyle{ 
  padding-top: 10px;
}
// 
</style>
