<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="银行代码" prop="bankCode">
        <el-input
          v-model="queryParams.bankCode"
          placeholder="请输入银行代码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="接口类型" prop="interfaceType">
        <el-select v-model="queryParams.interfaceType" placeholder="请选择接口类型" clearable>
          <el-option label="直连" value="DIRECT" />
          <el-option label="网关" value="GATEWAY" />
          <el-option label="代理" value="PROXY" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="isEnabled">
        <el-select v-model="queryParams.isEnabled" placeholder="请选择状态" clearable>
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          @click="handleAdd"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-circle-check"
          size="mini"
          :disabled="multiple"
          @click="handleEnable"
        >启用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-circle-close"
          size="mini"
          :disabled="multiple"
          @click="handleDisable"
        >禁用</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-connection"
          size="mini"
          :disabled="single"
          @click="handleTestConnection"
        >测试连接</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置ID" align="center" prop="configId" width="80" />
      <el-table-column label="银行代码" align="center" prop="bankCode" width="100" />
      <el-table-column label="银行名称" align="center" prop="bankName" width="150" />
      <el-table-column label="接口类型" align="center" prop="interfaceType" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.interfaceType === 'DIRECT'" type="primary">直连</el-tag>
          <el-tag v-else-if="scope.row.interfaceType === 'GATEWAY'" type="success">网关</el-tag>
          <el-tag v-else-if="scope.row.interfaceType === 'PROXY'" type="warning">代理</el-tag>
          <span v-else>{{ scope.row.interfaceType }}</span>
        </template>
      </el-table-column>
      <el-table-column label="接口名称" align="center" prop="interfaceName" width="150" />
      <el-table-column label="协议类型" align="center" prop="protocolType" width="100" />
      <el-table-column label="接口地址" align="center" prop="endpointUrl" width="200" show-overflow-tooltip />
      <el-table-column label="优先级" align="center" prop="priority" width="80" />
      <el-table-column label="负载权重" align="center" prop="loadBalanceWeight" width="80" />
      <el-table-column label="健康状态" align="center" prop="healthStatus" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.healthStatus === 'HEALTHY'" type="success">健康</el-tag>
          <el-tag v-else-if="scope.row.healthStatus === 'UNHEALTHY'" type="danger">不健康</el-tag>
          <el-tag v-else type="info">未知</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="isEnabled" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isEnabled"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="最后健康检查" align="center" prop="lastHealthCheckTime" width="160">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.lastHealthCheckTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="200">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
          >详情</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-connection"
            @click="handleTestConnection(scope.row)"
          >测试</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改银企联配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="银行代码" prop="bankCode">
              <el-input v-model="form.bankCode" placeholder="请输入银行代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行名称" prop="bankName">
              <el-input v-model="form.bankName" placeholder="请输入银行名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="接口类型" prop="interfaceType">
              <el-select v-model="form.interfaceType" placeholder="请选择接口类型">
                <el-option label="直连" value="DIRECT" />
                <el-option label="网关" value="GATEWAY" />
                <el-option label="代理" value="PROXY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口名称" prop="interfaceName">
              <el-input v-model="form.interfaceName" placeholder="请输入接口名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="协议类型" prop="protocolType">
              <el-select v-model="form.protocolType" placeholder="请选择协议类型">
                <el-option label="HTTPS" value="HTTPS" />
                <el-option label="SFTP" value="SFTP" />
                <el-option label="WebService" value="WEBSERVICE" />
                <el-option label="Socket" value="SOCKET" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="接口地址" prop="endpointUrl">
              <el-input v-model="form.endpointUrl" placeholder="请输入接口地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="连接超时(秒)" prop="connectionTimeout">
              <el-input-number v-model="form.connectionTimeout" :min="1" :max="300" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="读取超时(秒)" prop="readTimeout">
              <el-input-number v-model="form.readTimeout" :min="1" :max="600" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="最大重试次数" prop="maxRetryCount">
              <el-input-number v-model="form.maxRetryCount" :min="0" :max="10" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级" prop="priority">
              <el-input-number v-model="form.priority" :min="1" :max="10" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="负载权重" prop="loadBalanceWeight">
              <el-input-number v-model="form.loadBalanceWeight" :min="1" :max="100" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="认证类型" prop="authType">
              <el-select v-model="form.authType" placeholder="请选择认证类型">
                <el-option label="无认证" value="NONE" />
                <el-option label="基础认证" value="BASIC" />
                <el-option label="Token认证" value="TOKEN" />
                <el-option label="证书认证" value="CERTIFICATE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="健康检查地址" prop="healthCheckUrl">
              <el-input v-model="form.healthCheckUrl" placeholder="请输入健康检查地址" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查间隔(秒)" prop="healthCheckInterval">
              <el-input-number v-model="form.healthCheckInterval" :min="1" :max="86400" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="认证配置" prop="authConfig">
          <el-input v-model="form.authConfig" type="textarea" placeholder="请输入认证配置(JSON格式)" />
        </el-form-item>
        <el-form-item label="SSL配置" prop="sslConfig">
          <el-input v-model="form.sslConfig" type="textarea" placeholder="请输入SSL配置(JSON格式)" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog title="银企联配置详情" :visible.sync="detailOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="配置ID">{{ detailData.configId }}</el-descriptions-item>
        <el-descriptions-item label="银行代码">{{ detailData.bankCode }}</el-descriptions-item>
        <el-descriptions-item label="银行名称">{{ detailData.bankName }}</el-descriptions-item>
        <el-descriptions-item label="接口类型">{{ detailData.interfaceType }}</el-descriptions-item>
        <el-descriptions-item label="接口名称">{{ detailData.interfaceName }}</el-descriptions-item>
        <el-descriptions-item label="协议类型">{{ detailData.protocolType }}</el-descriptions-item>
        <el-descriptions-item label="接口地址" :span="2">{{ detailData.endpointUrl }}</el-descriptions-item>
        <el-descriptions-item label="连接超时">{{ detailData.connectionTimeout }}秒</el-descriptions-item>
        <el-descriptions-item label="读取超时">{{ detailData.readTimeout }}秒</el-descriptions-item>
        <el-descriptions-item label="最大重试次数">{{ detailData.maxRetryCount }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ detailData.priority }}</el-descriptions-item>
        <el-descriptions-item label="负载权重">{{ detailData.loadBalanceWeight }}</el-descriptions-item>
        <el-descriptions-item label="认证类型">{{ detailData.authType }}</el-descriptions-item>
        <el-descriptions-item label="健康状态">{{ detailData.healthStatus }}</el-descriptions-item>
        <el-descriptions-item label="是否启用">{{ detailData.isEnabled ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="健康检查地址" :span="2">{{ detailData.healthCheckUrl }}</el-descriptions-item>
        <el-descriptions-item label="检查间隔">{{ detailData.healthCheckInterval }}秒</el-descriptions-item>
        <el-descriptions-item label="最后检查时间">{{ detailData.lastHealthCheckTime }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ detailData.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ detailData.updateTime }}</el-descriptions-item>
        <el-descriptions-item label="认证配置" :span="2">
          <pre>{{ detailData.authConfig }}</pre>
        </el-descriptions-item>
        <el-descriptions-item label="SSL配置" :span="2">
          <pre>{{ detailData.sslConfig }}</pre>
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>

    <!-- 连接测试结果对话框 -->
    <el-dialog title="连接测试结果" :visible.sync="testResultOpen" width="600px" append-to-body>
      <el-result
        :icon="testResult.success ? 'success' : 'error'"
        :title="testResult.success ? '连接成功' : '连接失败'"
        :sub-title="testResult.message"
      >
        <template slot="extra">
          <el-descriptions :column="1" border v-if="testResult.success">
            <el-descriptions-item label="响应时间">{{ testResult.responseTime }}ms</el-descriptions-item>
            <el-descriptions-item label="测试时间">{{ testResult.timestamp }}</el-descriptions-item>
          </el-descriptions>
        </template>
      </el-result>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBankInterfaceConfigPage,
  getBankInterfaceConfigById,
  addBankInterfaceConfig,
  updateBankInterfaceConfig,
  deleteBankInterfaceConfig,
  enableBankInterfaceConfig,
  disableBankInterfaceConfig,
  testBankInterfaceConnection
} from "@/api/globalTreasurer/jspt";
import Pagination from "@/components/Pagination";

export default {
  name: "BankInterfaceConfig",
  components: {
    Pagination
  },
  directives: {
    hasPermi: {
      inserted(el, binding) {
        const { value } = binding;
        const permissions = JSON.parse(sessionStorage.getItem('permissions') || '[]');
        if (value && value instanceof Array && value.length > 0) {
          const hasPermission = permissions.some(permission => {
            return value.includes(permission);
          });
          if (!hasPermission) {
            el.parentNode && el.parentNode.removeChild(el);
          }
        }
      }
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 银企联配置表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 详情弹出层
      detailOpen: false,
      // 测试结果弹出层
      testResultOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        bankCode: null,
        interfaceType: null,
        isEnabled: null
      },
      // 表单参数
      form: {},
      // 详情数据
      detailData: {},
      // 测试结果
      testResult: {},
      // 表单校验
      rules: {
        bankCode: [
          { required: true, message: "银行代码不能为空", trigger: "blur" }
        ],
        bankName: [
          { required: true, message: "银行名称不能为空", trigger: "blur" }
        ],
        interfaceType: [
          { required: true, message: "接口类型不能为空", trigger: "change" }
        ],
        interfaceName: [
          { required: true, message: "接口名称不能为空", trigger: "blur" }
        ],
        protocolType: [
          { required: true, message: "协议类型不能为空", trigger: "change" }
        ],
        endpointUrl: [
          { required: true, message: "接口地址不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 格式化时间 */
    formatDateTime(time) {
      if (!time) return ''
      const date = new Date(time)
      if (isNaN(date.getTime())) return time
      const y = date.getFullYear()
      const m = String(date.getMonth() + 1).padStart(2, '0')
      const d = String(date.getDate()).padStart(2, '0')
      const h = String(date.getHours()).padStart(2, '0')
      const i = String(date.getMinutes()).padStart(2, '0')
      const s = String(date.getSeconds()).padStart(2, '0')
      return `${y}-${m}-${d} ${h}:${i}:${s}`
    },
    /** 查询银企联配置列表 */
    getList() {
      console.log('=== yqlpz.vue getList 调用 ===');
      console.log('queryParams:', this.queryParams);
      this.loading = true;
      getBankInterfaceConfigPage(this.queryParams).then(response => {
        console.log('=== API响应 ===', response);
        console.log('response.data:', response.data);
        console.log('response.data.rows:', response.data.rows);
        console.log('response.data.total:', response.data.total);

        this.configList = response.data.rows;
        this.total = response.data.total;

        console.log('=== 赋值后 ===');
        console.log('this.configList:', this.configList);
        console.log('this.total:', this.total);

        this.loading = false;
      }).catch(error => {
        console.error('=== API错误 ===', error);
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        configId: null,
        bankCode: null,
        bankName: null,
        interfaceType: null,
        interfaceName: null,
        protocolType: null,
        endpointUrl: null,
        connectionTimeout: 30,
        readTimeout: 60,
        maxRetryCount: 3,
        authType: "NONE",
        priority: 5,
        loadBalanceWeight: 10,
        healthCheckInterval: 60,
        isEnabled: 1
      };
      this.$nextTick(() => {
        this.$refs["form"] && this.$refs["form"].clearValidate();
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加银企联配置";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const configId = (row && row.configId) ? row.configId : this.ids[0];
      getBankInterfaceConfigById(configId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改银企联配置";
      });
    },
    /** 详情按钮操作 */
    handleView(row) {
      getBankInterfaceConfigById(row.configId).then(response => {
        this.detailData = response.data;
        this.detailOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.configId != null) {
            updateBankInterfaceConfig(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addBankInterfaceConfig(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = (row && row.configId) ? row.configId : this.ids.join(',');
      this.$modal.confirm('是否确认删除银企联配置编号为"' + configIds + '"的数据项？').then(function() {
        return deleteBankInterfaceConfig(configIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 启用按钮操作 */
    handleEnable() {
      enableBankInterfaceConfig(this.ids).then(() => {
        this.getList();
        this.$modal.msgSuccess("启用成功");
      });
    },
    /** 禁用按钮操作 */
    handleDisable() {
      disableBankInterfaceConfig(this.ids).then(() => {
        this.getList();
        this.$modal.msgSuccess("禁用成功");
      });
    },
    /** 状态修改 */
    handleStatusChange(row) {
      let text = row.isEnabled === 1 ? "启用" : "停用";
      this.$modal.confirm('确认要"' + text + '""' + row.bankName + '"配置吗？').then(function() {
        const ids = [row.configId];
        if (row.isEnabled === 1) {
          return enableBankInterfaceConfig(ids);
        } else {
          return disableBankInterfaceConfig(ids);
        }
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function() {
        row.isEnabled = row.isEnabled === 0 ? 1 : 0;
      });
    },
    /** 测试连接 */
    handleTestConnection(row) {
      const configId = row && row.configId ? row.configId : this.ids[0];
      if (!configId) {
        this.$message.warning("请先选择一条配置");
        return;
      }
      const loading = this.$loading({ lock: true, text: "正在测试连接...", spinner: "el-icon-loading", background: "rgba(0,0,0,0.3)" });
      testBankInterfaceConnection(configId).then(response => {
        loading.close();
        this.testResult = response.data || {};
        this.testResultOpen = true;
      }).catch(() => {
        loading.close();
        this.$message.error("测试连接失败");
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.$modal.confirm('是否确认导出银企联配置数据?').then(() => {
        import('@/utils/request').then(({ default: request }) => {
          request({
            url: '/qqsk/settlement/bank-interface-config/export',
            method: 'post',
            data: this.queryParams,
            responseType: 'blob'
          }).then(response => {
            const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
            const url = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = url;
            link.download = `银企联配置_${new Date().getTime()}.xlsx`;
            link.click();
            window.URL.revokeObjectURL(url);
            this.$modal.msgSuccess("导出成功");
          }).catch(() => {});
        });
      }).catch(() => {});
    }
  }
};
</script>
