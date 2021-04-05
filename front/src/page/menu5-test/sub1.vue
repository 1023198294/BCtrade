<template>
  <div class="table-content">
  <el-table
    v-loading="loadingPitch"
    :data="pageList"
    size="mini" border style="width: 100%"
    :row-key="(row)=>{return row.id}">
    <el-table-column prop="id" label=" " width="100"></el-table-column>
    <el-table-column prop="reportId" label="举报记录ID" width="140"></el-table-column>
    <el-table-column prop="reporterId" label="举报人ID" width="140"></el-table-column>
    <el-table-column prop="reportedId" label="被举报人ID" width="100"></el-table-column>
    <el-table-column prop="dataid" label="被举报数据ID"></el-table-column>
    <el-table-column fixed="right" label="操作" width="400">
      <template slot-scope="scope">
        <el-button @click="handleCheck(scope.row)" type="text" size="small">详细</el-button>
        <el-button @click="handleDownload(scope.row)" type="text" size="small">下载</el-button>
        <el-button @click="ban(scope.row)" type="text" size="small">封禁</el-button>
      </template>
    </el-table-column>

  </el-table>
    <el-dialog title="举报信息" :visible.sync="diagLogVisible">
      <h2>
        举报原因
      </h2>
      <p>
        {{this.form.type}}
      </p>
      <h2>
        举报描述
      </h2>
      <p>
        {{this.form.description}}
      </p>
    </el-dialog>
    <el-dialog title="举报信息" :visible.sync="banDialogVisible">
      <el-checkbox v-model="banUser">封禁用户</el-checkbox>
      <el-checkbox v-model="banData">封禁数据</el-checkbox>
      <el-button @click="handleBan" style="margin-left: 100px">确认执行</el-button>
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
      banUser: false,
      banData: false,
      banRow: '',
      banDialogVisible:false,
      loadingPitch : true,
      pageList :[],
      currentPage : 0,
      pageSize: 40,
      diagLogVisible: false,
      form:{
        type:'',
        description:''
      },
      dtid:'',
    }
  },
  methods:{
    handleRefresh(){
      var _this = this
      this.pageList = []
      this.$axios({
        method:'post',
        url:this.$global.baseUrl+'/admin/getReportList',
        params:{
          page:this.currentPage,
          pageSize:this.pageSize
        }
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
              dataid: res.data[idx].dataId,
              description: res.data[idx].description,
              reportId: res.data[idx].reportId,
              reporterId : res.data[idx].reporterId,
              reportedId: res.data[idx].reportedId,
              type: res.data[idx].type,
              banUser:false,
              banData:false,
              available: true,
            }
          this.pageList.push(temp)
        }
      })
      this.loadingPitch = false
    },
    ban(row){
      this.banRow = row.id
      this.banDialogVisible = true
    },
    handleCheck(row){
      this.form.type = this.pageList[row.id].type
      this.form.description = this.pageList[row.id].description
      this.diagLogVisible = true
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
    handleBan(){
      console.log(this.pageList)
      this.$axios({
        method:'get',
        url:this.$global.baseUrl+'/admin/ban',
        params:{
          reportId:this.pageList[this.banRow].reportId,
          banUser:this.banUser,
          banData:this.banData
        }
        //String ban(int reportId,boolean banUser,boolean banData)
      }).then((res)=>{
        console.log(res)
        if(res.data === "ban error"){
          this.$notify({
            message:"封禁失败",
            type:"error",
          })
        }else{
          this.$notify({
            message:"封禁成功",
            type:"success",
          })
        }
        this.banDialogVisible = false
        this.handleRefresh()
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
