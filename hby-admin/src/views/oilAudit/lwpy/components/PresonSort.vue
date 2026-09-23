<template>
  <el-dialog :visible.sync="dialogVisible" :modal="modal" width="800px" :close-on-click-modal="false">
    <div class="system-log-container">
      <div class="lr-layout">
        <div class="right">
          <el-table v-loading="listLoading" :data="list" style="width: 100%">
            <el-table-column align="center" label="用户真实名" prop="realname"></el-table-column>
            <el-table-column align="center" prop="fs" label="分数">
              <template slot-scope="scope">
                <el-input v-if="scope.row.staffid == userId" @input="handleInput(scope.$index, scope.row)" v-model="scope.row.fs" size="mini"
                  style="width: 90%" type="number" />
                <div v-else>{{ scope.row.fs }}</div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button @click="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </template>
  </el-dialog>
</template>
<script>
import { fssaveOrUpdate, getLwpxfsList } from '@/oapi/audit/lwpy'
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
  data() {
    const userId = JSON.parse(localStorage.getItem('userInfo')).staffid
    return {
      listLoading: false,
      userId,
      dialogVisible: false,
      list: [],
      dataIndex: null,
      perid: null,
      changeIndex: null
    }
  },
  methods: {
    async showEdit(listData, id, index) {
      console.log(this.userId)
      console.log(listData, 'e', id)
      this.dataIndex = null
      this.perid = id
      this.changeIndex = index
      this.dialogVisible = true
      // getLwpxfsList  pxid

      // const index = listData.findIndex(i => i.staffid === this.userId);  
      // if (index !== -1) {  
      //   this.dataIndex = index;  
      // }

      this.dataIndex = listData.findIndex(i => i.staffid == this.userId);
      console.log(this.dataIndex)

      let res = await getLwpxfsList({perid: id})

      listData.forEach((j, i) => {
        res.data.data.forEach((k, h) => {
          if (j.staffid == k.pwstaffid) {
            listData[i].fs = k.fs
            listData[i].pxid = k.pxid
          }
        })
      })

      this.list = listData
    },

    /**
     * @description: 保存表单
     * @return {*}
     */
    async save() {
      console.log(this.dataIndex)
      if(!this.list[this.dataIndex].fs) {
        this.$baseMessage(
          '请打分数！',
          'error',
          'vab-hey-message-error'
        )
        return false
      }
      let params = {
        perid: this.perid,
        pwname: this.list[this.dataIndex].realname,
        pwstaffid: this.list[this.dataIndex].staffid,
        fs: this.list[this.dataIndex].fs,
        pxid: this.list[this.dataIndex].pxid,
      }
      const res = await fssaveOrUpdate(params)
      if (res.code == 1) {
        const sum = this.list.reduce((accumulator, currentValue) => { 
        const num = Number(currentValue.fs);  
          if (!isNaN(num) && isFinite(num)) {  
            return accumulator + num;  
          }
          return accumulator;  
        }, 0);
        console.log(sum)
        this.$emit('selected', { index: this.changeIndex, fs: sum })
        this.dialogVisible = false
        this.$baseMessage('成功', 'success')
      }
      // const info = this.list.map((res) => {
      //   return { ...res, userId: res.staffid || res.userId }
      // })
      // 

      
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

.lr-layout>.left {
  width: 350px;
  border-right: 1px solid ghostwhite;
  margin-right: 100px;
  padding-right: 10px;
}

.lr-layout>.right {
  flex: 1;
}
</style>
