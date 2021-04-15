<template>
  <div class="table-content">
    <div class="demo-input-suffix">
      <el-input v-model="uid"></el-input><el-button @click="handleSBUN">用户名搜索</el-button> <el-input v-model="did"></el-input> <el-button @click="handleSBDN">数据名搜索</el-button>
    </div>
    <el-table
      v-loading="loadingPitch"
      :data="pageList"
      size="mini" border style="width: 100%"
      :row-key="(row)=>{return row.id}">
      <el-table-column prop="id" label=" " width="100"></el-table-column>
      dataId: res.data[idx].dataId,
      fromId: res.data[idx].fromId,
      toId: res.data[idx].toId,
      creatorId : res.data[idx].creatorId,
      rate: res.data[idx].rate,
      value: res.data[idx].value,
      <el-table-column prop="dataId" label="交易数据ID" ></el-table-column>
      <el-table-column prop="fromId" label="资产售卖者ID" ></el-table-column>
      <el-table-column prop="toId" label="资产购买者ID" ></el-table-column>
      <el-table-column prop="creatorId" label="资产原拥有者ID"></el-table-column>
      <el-table-column prop="rate" label="利润分配率"></el-table-column>
      <el-table-column prop="value" label="利润分配率"></el-table-column>
      <el-table-column fixed="right" label="操作" width="400">
        <template slot-scope="scope">
          <el-button @click="handleCheck(scope.row)" type="text" size="small">数据</el-button>
          <el-button @click="handleDownload(scope.row)" type="text" size="small">下载</el-button>
          <el-button @click="report(scope.row)" type="text" size="small">报警</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="信息详情" :visible.sync="diagLogVisible">
      <h2>
        涉及金额
      </h2>
      <p>
        {{this.involved_money}}
      </p>
      <h2>
        涉及交易数量
      </h2>
      <p>
        {{this.involved_cases}}
      </p>
    </el-dialog>
  </div>
</template>
<script>
export default {
  created() {
    this.handleRefresh()
  },
  data(){

    return{
      uid:'',
      did:'',
      filterMode : 'default',
      loadingPitch : true,
      pageList :[],
      currentPage : 0,
      pageSize: 40,
      involved_money:'loading....',
      involved_cases:'loading....',
      diagLogVisible: false,
      form:{
        type:'',
        description:''
      },
      dtid:'',
    }
  },
  methods:{
    handleSBUN(){
      this.filterMode = 'byName'
      this.handleRefresh()
    },
    handleSBDN(){
      this.filterMode = 'byData'
      this.handleRefresh()
    },
    handleRefresh(){
      var _this = this
      this.pageList = []
      var url = ''
      var params = {}
      switch (this.filterMode){
        case 'byName':
          url = this.$global.baseUrl+'/admin/get_user_trades'
          params ={
            userId : this.uid,
            page:this.currentPage,
            pageSize:this.pageSize
          }
          break
        case 'byData':
          url = this.$global.baseUrl+'/admin/get_data_trades'
          params ={
            dataId : this.did,
            page:this.currentPage,
            pageSize:this.pageSize
          }
          break
        case 'default':
          default:
            url = this.$global.baseUrl+'/admin/get_all_trades'
            params={
              page:this.currentPage,
                pageSize:this.pageSize
            }
      }
      this.$axios({
        method:'post',
        url:url,
        params:params
      }).then((res)=>{
        console.log(res)
        this.pageCount = Math.floor(Math.round(res.data.page)/Math.round(this.pageSize))+1
        var indexes = res.data.page;
        if(indexes===(this.pageCount-1)*this.pageSize){
          this.pageCount-=1
        }
        if(indexes<this.pageSize){
        } else if(indexes>this.pageSize && this.currentPage===this.pageCount){
          indexes = indexes % this.pageSize
        }else{
          indexes = this.pageSize
        }
        //console.log(indexes,this.pageCount,this.pageSize)
        for(var i=0;i<indexes;i++){
          const idx = 'data'+i
          let temp=
            {
              id: i,
              dataId: res.data[idx].dataId,
              fromId: res.data[idx].fromId,
              toId: res.data[idx].toId,
              creatorId : res.data[idx].creatorId,
              rate: res.data[idx].rate,
              value: res.data[idx].value,
              available: true,
            }
          this.pageList.push(temp)
        }
      })
      this.loadingPitch = false
    },
    handleCheck(row){

      this.$axios(
        {
          method:'get',
          url:this.$global.baseUrl+'/admin/get_money_involved',
          params:{
            dataId:this.pageList[row.id].dataId
          }
        }
      ).then((res)=>{
        console.log(res);
        this.involved_money = res.data.value+' BCT'
        this.involved_cases = res.data.cases+' 单交易'
      })
      this.diagLogVisible = true
      //this.form.type = this.pageList[row.id].type
      //this.form.description = this.pageList[row.id].description
    },
    handleDownload(row) {
      this.dtid = this.pageList[row.id].dataid
      //console.log(dtid)
      this.$axios({
        method:'get',
        url:this.$global.baseUrl+'/admin/getRealName',
        params:{
          dataid:this.dtid
        }
      }).then((res)=>{
        this.dtrn = res.data
      })

      this.$axios({
        method:'get',
        url:this.$global.baseUrl+'/admin/Download',
        params:{
          dataId:this.dtid
        },
        responseType:'blob'
      }).then((res)=>{
        const blob = res.data
        const reader = new FileReader()
        reader.readAsDataURL(blob)
        reader.onload = (e) => {
          const a = document.createElement('a')
          a.download = this.dtrn
          a.href = e.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
        }
      }).catch((error)=>console.log(error))
    },
    report(){
      this.$confirm('确认将用户、数据等信息上报公安机关？','提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '上报成功!'
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消上报'
        });
      })
    }
  }
}
</script>
<style>
.page-container {
  font-size: 20px;
  text-align: center;
  color: rgb(192, 204, 218);
}
</style>
