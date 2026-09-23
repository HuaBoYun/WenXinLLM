<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" type="card" @tab-click="handleTabClick">
      <!-- 资金池管理 -->
      <el-tab-pane label="资金池管理" name="fundPool">
        <div class="fund-pool-container">
          <!-- 查询条件 -->
          <el-form :model="fundPoolQuery" ref="fundPoolQueryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
            <el-form-item label="资金池名称" prop="poolName">
              <el-input
                v-model="fundPoolQuery.poolName"
                placeholder="请输入资金池名称"
                clearable
                @keyup.enter.native="handleFundPoolQuery"
              />
            </el-form-item>
            <el-form-item label="池状态" prop="poolStatus">
              <el-select v-model="fundPoolQuery.poolStatus" placeholder="请选择池状态" clearable>
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
                <el-option label="关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" size="mini" @click="handleFundPoolQuery">搜索</el-button>
              <el-button icon="el-icon-refresh" size="mini" @click="resetFundPoolQuery">重置</el-button>
            </el-form-item>
          </el-form>

          <!-- 操作按钮 -->
          <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
              <el-button
                type="primary"
                plain
                icon="el-icon-plus"
                size="mini"
                @click="handleFundPoolAdd"
              >新增资金池</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="success"
                plain
                icon="el-icon-edit"
                size="mini"
                :disabled="fundPoolSingle"
                @click="handleFundPoolUpdate"
              >修改</el-button>
            </el-col>
            <el-col :span="1.5">
              <el-button
                type="danger"
                plain
                icon="el-icon-delete"
                size="mini"
                :disabled="fundPoolMultiple"
                @click="handleFundPoolDelete"
              >删除</el-button>
            </el-col>
          </el-row>

          <!-- 数据表格 -->
          <el-table v-loading="fundPoolLoading" :data="fundPoolList" @selection-change="handleFundPoolSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="资金池编号" align="center" prop="poolCode" />
            <el-table-column label="资金池名称" align="center" prop="poolName" />
            <el-table-column label="池类型" align="center" prop="poolType">
              <template slot-scope="scope">
                <dict-tag :options="poolTypeOptions" :value="scope.row.poolType"/>
              </template>
            </el-table-column>
            <el-table-column label="总余额" align="center" prop="totalBalance">
              <template slot-scope="scope">
                <span>{{ formatAmount(scope.row.totalBalance) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="成员数量" align="center" prop="memberCount" />
            <el-table-column label="池状态" align="center" prop="poolStatus">
              <template slot-scope="scope">
                <dict-tag :options="poolStatusOptions" :value="scope.row.poolStatus"/>
              </template>
            </el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" width="180">
              <template slot-scope="scope">
                <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-view"
                  @click="handleFundPoolView(scope.row)"
                >查看</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-edit"
                  @click="handleFundPoolUpdate(scope.row)"
                >修改</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-delete"
                  @click="handleFundPoolDelete(scope.row)"
                >删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination
            background
            class="pagination"
            :current-page="fundPoolQuery.pageNum"
            :layout="layout"
            :page-size="fundPoolQuery.pageSize"
            :total="fundPoolTotal"
            @current-change="handleFundPoolCurrentChange"
            @size-change="handleFundPoolSizeChange"
          />
        </div>
      </el-tab-pane>

      <!-- 资金归集 -->
      <el-tab-pane label="资金归集" name="fundConcentration">
        <div class="fund-concentration-container">
          <p>资金归集功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 资金下拨 -->
      <el-tab-pane label="资金下拨" name="fundAllocation">
        <div class="fund-allocation-container">
          <p>资金下拨功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 内部借贷 -->
      <el-tab-pane label="内部借贷" name="internalLending">
        <div class="internal-lending-container">
          <p>内部借贷功能开发中...</p>
        </div>
      </el-tab-pane>

      <!-- 资金监控 -->
      <el-tab-pane label="资金监控" name="fundMonitoring">
        <div class="fund-monitoring-container">
          <p>资金监控功能开发中...</p>
        </div>
      </el-tab-pane>
    </el-tabs>

    <!-- 添加或修改资金池对话框 -->
    <el-dialog :title="fundPoolTitle" :visible.sync="fundPoolOpen" width="800px" append-to-body>
      <el-form ref="fundPoolForm" :model="fundPoolForm" :rules="fundPoolRules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="资金池名称" prop="poolName">
              <el-input v-model="fundPoolForm.poolName" placeholder="请输入资金池名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="池类型" prop="poolType">
              <el-select v-model="fundPoolForm.poolType" placeholder="请选择池类型">
                <el-option label="实体资金池" value="PHYSICAL" />
                <el-option label="虚拟资金池" value="VIRTUAL" />
                <el-option label="境外资金池" value="OFFSHORE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="主账户" prop="masterAccount">
              <el-input v-model="fundPoolForm.masterAccount" placeholder="请输入主账户" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="池状态" prop="poolStatus">
              <el-select v-model="fundPoolForm.poolStatus" placeholder="请选择池状态">
                <el-option label="活跃" value="ACTIVE" />
                <el-option label="暂停" value="SUSPENDED" />
                <el-option label="关闭" value="CLOSED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="池描述" prop="poolDescription">
          <el-input v-model="fundPoolForm.poolDescription" type="textarea" placeholder="请输入池描述" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="fundPoolForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFundPoolForm">确 定</el-button>
        <el-button @click="cancelFundPool">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getFundPoolPage,
  getFundPool,
  createFundPool,
  updateFundPool,
  deleteFundPool
} from "@/api/globalTreasurer/zjjz";
import { parseTime } from '@/utils'

export default {
  name: "FundConcentration",
  data() {
    return {
      // 当前激活的标签页
      activeTab: "fundPool",
      // 显示搜索条件
      showSearch: true,
      // 分页布局
      layout: "total, sizes, prev, pager, next, jumper",
      
      // 资金池相关数据
      fundPoolLoading: true,
      fundPoolIds: [],
      fundPoolSingle: true,
      fundPoolMultiple: true,
      fundPoolTotal: 0,
      fundPoolList: [],
      fundPoolTitle: "",
      fundPoolOpen: false,
      fundPoolQuery: {
        pageNum: 1,
        pageSize: 10,
        poolName: null,
        poolStatus: null
      },
      fundPoolForm: {},
      fundPoolRules: {
        poolName: [
          { required: true, message: "资金池名称不能为空", trigger: "blur" }
        ],
        poolType: [
          { required: true, message: "池类型不能为空", trigger: "change" }
        ],
        masterAccount: [
          { required: true, message: "主账户不能为空", trigger: "blur" }
        ],
        poolStatus: [
          { required: true, message: "池状态不能为空", trigger: "change" }
        ]
      },
      
      // 字典选项
      poolTypeOptions: [
        { label: "实体资金池", value: "PHYSICAL" },
        { label: "虚拟资金池", value: "VIRTUAL" },
        { label: "境外资金池", value: "OFFSHORE" }
      ],
      poolStatusOptions: [
        { label: "活跃", value: "ACTIVE" },
        { label: "暂停", value: "SUSPENDED" },
        { label: "关闭", value: "CLOSED" }
      ]
    };
  },
  created() {
    this.getFundPoolList();
  },
  methods: {
    parseTime,
    
    /** 格式化金额 */
    formatAmount(amount) {
      if (!amount) return '0.00';
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }) + ' 元';
    },
    
    /** 标签页切换 */
    handleTabClick(tab) {
      if (tab.name === 'fundPool') {
        this.getFundPoolList();
      }
    },

    /** 查询资金池列表 */
    async getFundPoolList() {
      this.fundPoolLoading = true;
      try {
        const response = await getFundPoolPage(this.fundPoolQuery);
        
        // 使用与配置文件一致的成功状态码
        const successCodes = [200, 0, '200', '0', '1', 1, 2];
        if (successCodes.includes(response.code)) {
          // 支持多种数据结构格式
          if (response.data && response.data.tlist !== undefined) {
            // PageInfo格式：{ code: 200, data: { tlist: [], totalRecord: 0 } }
            this.fundPoolList = response.data.tlist || [];
            this.fundPoolTotal = response.data.totalRecord || 0;
          } else if (response.data && response.data.list !== undefined) {
            // 标准格式：{ code: 200, data: { list: [], total: 0 } }
            this.fundPoolList = response.data.list || [];
            this.fundPoolTotal = response.data.total || 0;
          } else if (response.data && Array.isArray(response.data)) {
            // 数组格式：{ code: 200, data: [] }
            this.fundPoolList = response.data || [];
            this.fundPoolTotal = response.data.length || 0;
          } else if (response.tlist !== undefined) {
            // 直接PageInfo格式：{ code: 200, tlist: [], totalRecord: 0 }
            this.fundPoolList = response.tlist || [];
            this.fundPoolTotal = response.totalRecord || 0;
          } else if (response.list !== undefined) {
            // 直接格式：{ code: 200, list: [], total: 0 }
            this.fundPoolList = response.list || [];
            this.fundPoolTotal = response.total || 0;
          } else {
            // 兜底处理
            this.fundPoolList = [];
            this.fundPoolTotal = 0;
          }
        } else {
          this.$message.error(response.message || response.msg || '查询失败');
          this.fundPoolList = [];
          this.fundPoolTotal = 0;
        }
      } catch (error) {
        console.error('获取资金池列表失败:', error);
        this.$message.error('获取数据失败：' + error.message);
        this.fundPoolList = [];
        this.fundPoolTotal = 0;
      }
      this.fundPoolLoading = false;
    },

    /** 搜索按钮操作 */
    handleFundPoolQuery() {
      this.fundPoolQuery.pageNum = 1;
      this.getFundPoolList();
    },

    /** 重置按钮操作 */
    resetFundPoolQuery() {
      this.resetForm("fundPoolQueryForm");
      this.handleFundPoolQuery();
    },

    /** 多选框选中数据 */
    handleFundPoolSelectionChange(selection) {
      this.fundPoolIds = selection.map(item => item.poolId);
      this.fundPoolSingle = selection.length !== 1;
      this.fundPoolMultiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleFundPoolAdd() {
      this.resetFundPoolForm();
      this.fundPoolOpen = true;
      this.fundPoolTitle = "添加资金池";
    },

    /** 修改按钮操作 */
    handleFundPoolUpdate(row) {
      this.resetFundPoolForm();
      const poolId = row.poolId || this.fundPoolIds;
      getFundPool(poolId).then(response => {
        this.fundPoolForm = response.data;
        this.fundPoolOpen = true;
        this.fundPoolTitle = "修改资金池";
      });
    },

    /** 查看按钮操作 */
    handleFundPoolView(row) {
      this.$router.push(`/globalTreasurer/zjjz/fundPool/detail/${row.poolId}`);
    },

    /** 提交按钮 */
    submitFundPoolForm() {
      this.$refs["fundPoolForm"].validate(valid => {
        if (valid) {
          if (this.fundPoolForm.poolId != null) {
            updateFundPool(this.fundPoolForm).then(response => {
              this.$message.success("修改成功");
              this.fundPoolOpen = false;
              this.getFundPoolList();
            });
          } else {
            createFundPool(this.fundPoolForm).then(response => {
              this.$message.success("新增成功");
              this.fundPoolOpen = false;
              this.getFundPoolList();
            });
          }
        }
      });
    },

    /** 删除按钮操作 */
    handleFundPoolDelete(row) {
      const poolIds = row.poolId || this.fundPoolIds;
      this.$confirm('是否确认删除资金池编号为"' + poolIds + '"的数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return deleteFundPool(poolIds);
      }).then(() => {
        this.getFundPoolList();
        this.$message.success("删除成功");
      }).catch(() => {});
    },

    /** 取消按钮 */
    cancelFundPool() {
      this.fundPoolOpen = false;
      this.resetFundPoolForm();
    },

    /** 表单重置 */
    resetFundPoolForm() {
      this.fundPoolForm = {
        poolId: null,
        poolName: null,
        poolType: null,
        masterAccount: null,
        poolStatus: "ACTIVE",
        poolDescription: null,
        remark: null
      };
      this.resetForm("fundPoolForm");
    },

    /** 分页相关方法 */
    handleFundPoolCurrentChange(val) {
      this.fundPoolQuery.pageNum = val;
      this.getFundPoolList();
    },

    handleFundPoolSizeChange(val) {
      this.fundPoolQuery.pageSize = val;
      this.getFundPoolList();
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.mb8 {
  margin-bottom: 8px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}
</style>
