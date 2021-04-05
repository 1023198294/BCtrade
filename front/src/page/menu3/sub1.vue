<template>
  <div class="table-content">
    <el-input type="text" placeholder="随时随地发现新数据" v-model="searchValue" @keyup.enter.native="searchHandler"></el-input>

    <el-table v-loading="loadingPitch" :data="pageList" size="mini" border style="width: 100%">
      <el-table-column prop="id" label=" " width="100"></el-table-column>
      <el-table-column prop="dataid" label="数据ID" width="140"></el-table-column>
      <el-table-column prop="dataname" label="数据名称" width="140"></el-table-column>
      <el-table-column prop="size" label="数据大小" width="100"></el-table-column>
      <el-table-column prop="value" label="价值"></el-table-column>
      <el-table-column fixed="right" label="操作" width="200">
        <template slot-scope="scope">
          <el-button @click="handleCheck(scope.row)" type="text" size="small">详细</el-button>
          <el-button @click="purchase(scope.row)" type="text" size="small">购买</el-button>
          <el-button @click="report(scope.row)" type="text" size="small">举报</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      small
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="pageSize*pageCount"
      >
    </el-pagination>
    <el-dialog title="举报信息" :visible.sync="dialogFormVisible">
      <el-form :model="form" ref="form">
        <el-form-item label="违规类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择">
            <el-option
              v-for="item in typeOptions"
              :key="item.value"
              :label="item.value"
              :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="违规数据描述" prop="description">
          <el-input type="textarea" :rows="5" placeholder="请输入违规数据描述" v-model="form.description"></el-input>
        </el-form-item>
        <el-button @click="handleReport(reportRow)">确认</el-button>
      </el-form>
    </el-dialog>
    </div>

</template>

<script>
export default {
name: "sub1",
  data(){
    return {
      hasInit: false,
      pageList:[],
      searchValue:'',
      pageSize:5,
      currentPage:1,
      pageCount:1,
      loadingPitch:false,
      dialogFormVisible:false,
      reportRow:0,
      form:{
        type:'',
        reportedid:'',
        dataid:'',
        description:''
      },
      typeOptions:[{
        value: '剽窃',
        label: 'label1'
        },{
        //剽窃，恶意篡改，数据含敏感信息，数据涉嫌泄密，涉嫌洗钱
        value: '恶意篡改他人数据',
        label: 'label2'
      },{
        value: '数据含敏感信息',
        label: 'label3'
      },{
        value: '数据涉嫌泄密',
        label: 'label4'
      },{
        value: '涉嫌洗钱',
        label: 'label5'
      }],
    }
  },
  methods:{
    handleReport(row){
      this.$axios({
        method:'post',
        url:this.$global.baseUrl+"/admin/report",
        params:{
          type:this.form.type,
          dataid:this.pageList[row.id].dataid,
          description:this.form.description
        }
      }).then((res)=>{
        console.log(res)
        if(res.data==='done'){
          this.$notify({
            title:'notification',
            message:'举报成功，管理员将审核该举报信息',
            type:'success'
          })

        }else{
          this.$notify({
            title:'notification',
            message:'服务器错误，举报失败',
            type:'error'
          })
        }
        this.dialogFormVisible = false
      })
    },
    handleCheck(row){
      console.log(row)
      console.log(this.pageList[0])
      this.$message({
        type:"info",
        message:"dataname:"+row.dataname+"\ndescription:"+this.pageList[row.id].description
    })},
    purchase(row){
      var _this = this
      this.$confirm(
        '是否购买此数据？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(()=>{
        this.loadingPitch = true
        this.$axios({
          method:'post',
          url:_this.$global.baseUrl+"/admin/purchase",
          params:{
            source:this.pageList[row.id].dataid
          }
        }).then((res)=>{
          console.log(res)
          if(res.data==='success'){
            this.$message({
              type:"success",
              message:'购买成功!'
            })
          }else {
            this.$message({
              type:"error",
              message:'购买失败! '+res.data
            })
          }
          this.loadingPitch = false
        })
      })
    },
    report(row){
      var _this = this
      this.$confirm(
        '是否举报此数据？','提示',{
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(()=> {
        this.dialogFormVisible = true
        this.reportRow = row
      }
    )
    },
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.pageList = []
      this.searchHandler()
    },
    searchHandler(){
      this.pageList = []
      this.loadingPitch = true
        console.log("search "+ this.searchValue);
        this.$axios(
          {
            method:'post',
            url:this.$global.baseUrl+"/admin/search",
            params:{
              dname: this.searchValue,
              page:this.currentPage,
              pageSize:this.pageSize
            }
          }
        ).then((res)=>{
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
          console.log(indexes,this.pageCount,this.pageSize)

          for(var i=0;i<indexes;i++){
            const idx = 'data'+i
            if(res.data[idx]==null)
              break;
            let temp=
              {
                id:i,
                dataid:res.data[idx].dataid,
                dataname:res.data[idx].dataname,
                size:res.data[idx].size,
                value:res.data[idx].value,
                description:res.data[idx].description
              }

              this.pageList.push(temp)

            //console.log(res.data[idx])
          }

          //console.log(this.pageList)
          //console.log(this.tableData)
          this.loadingPitch = false
        })
      }
      /*this.$axios.get(this.$global.baseUrl+"/admin/remain").then(function (res){
        if(res.data==="余额查询错误"){
          _this.remain = res.data;
        }
        _this.remain = _this.toDecimal2(res.data);
      })*/
      /*this.$axios.get(this.$global.baseUrl+"/admin/search",{}).then(function (res){
        if(res.data==="失败"){
          this.$notify({
              title:'error',
              message:res,
              type:'error'
          })
        }
      })*/

  }
}
</script>

<style scoped>

</style>
