<template>
  <div class="system-log-container">
    <div class="search-view">
      <el-input
        v-model="keywords"
        class="input-with-select"
        placeholder="搜索公司名称"
        size="medium"
      >
        <el-button slot="append">望远镜</el-button>
      </el-input>
    </div>
    <el-row :gutter="20">
      <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>行业数据</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/industryData/index">望远镜</a>
          </div>
        </el-card>
      </el-col>
      <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>企业数据</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/companyData/statement">
              报表数据
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/companyData/accountData">
              账薄数据
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/companyData/accountManage">
              账薄管理
            </a>
          </div>
        </el-card>
      </el-col>
      <!-- <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>风险工具</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/contractTools/realime">
              实时分析
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/contractTools/risk">
              法律风险预测
            </a>
          </div>
        </el-card>
      </el-col> -->
      <!-- <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>审计工具</span>
          </div>

          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a
              class="link"
              href="http://impl.packbi.com:9001/reportCenter"
              target="_parent"
            >
              实时分析
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a
              class="link"
              href="http://impl.packbi.com:9001/reportCenter"
              target="_parent"
            >
              场景分析
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/audit">审计模板</a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/guide">指引模板</a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/category">
              审计类型维护
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/flaw">缺陷标准</a>
          </div>
        </el-card>
      </el-col> -->
      <!-- <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>监控工具</span>
          </div>
        </el-card>
      </el-col> -->
      <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>合规标准</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/magnifyingGlass">
              放大镜
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/auditTools/lawSearch">法律搜索</a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/law">法律规章</a>
          </div>

          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/flfgk">法律法规库</a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/jtgsgzzdk">
              集团规章制度
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/manage">管理制度</a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/knowledge">
              行业知识库
            </a>
          </div>
          <!-- <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/auditMode">
              审计模型库
            </a>
          </div> -->
          <!-- <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/knowledge">
              行业知识库
            </a>
          </div> -->
          <!-- <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/controlLib/experience">
              审计经验库
            </a>
          </div> -->
        </el-card>
      </el-col>
      <!-- <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>内控工具</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/internalTools/defect-standard">
              缺陷标准
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a
              class="link"
              href="#/workbench/internalTools/evaluation-template"
            >
              评价模板
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/internalTools/factor-maintenance">
              要素维护
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/internalTools/level-maintenance">
              等级维护
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/internalTools/test-template">
              测试模板
            </a>
          </div>
        </el-card>
      </el-col> -->
      <!-- <el-col :sm="8" :xs="24">
        <el-card class="box-card workbench-card">
          <div slot="header" class="clearfix">
            <span>工作底稿</span>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/workPaper/draftManage">
              底稿管理
            </a>
          </div>
          <div class="text item">
            <vab-icon class="link-icon" icon="focus-line" />
            <a class="link" href="#/workbench/workPaper/dubaManag">疑点管理</a>
          </div>
        </el-card>
      </el-col> -->
    </el-row>
  </div>
</template>

<script>
  export default {
    name: 'Index',
    data() {
      return {
        keywords: '',
      }
    },
  }
</script>

<style scoped>
  .search-view {
    padding: 30px;
    text-align: center;
  }
  .search-view .input-with-select {
    max-width: 500px;
    border-color: red !important;
  }
  .link-icon {
    font-size: 20px;
    color: #1e8bf1;
  }
  .link {
    font-size: 16px;
    margin-left: 10px;
    color: #0f2027;
  }
  .workbench-card {
    height: 300px;
  }
</style>
