<template>
  <div class="table-content">
    <el-table
      v-loading="loadingPitch"
      :data="pageList"
      size="mini" border style="width: 100%"
      :row-key="(row)=>{return row.id}">
      <el-table-column prop="id" label=" " width="100"></el-table-column>
      <el-table-column prop="userid" label="可疑用户ID" ></el-table-column>
      <el-table-column prop="money" label="金额" ></el-table-column>
      <el-table-column prop="action" label="用户操作"></el-table-column>
    </el-table>
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
      currentPage : 1,
      pageSize: 1000,
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
        url:this.$global.baseUrl+'/admin/getCA',
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
              userid: res.data[idx].userId,
              action: res.data[idx].action,
              money: res.data[idx].value,
            }
          this.pageList.push(temp)
        }
      })
      this.loadingPitch = false
    },
    handleCheck(row){
      this.form.type = this.pageList[row.id].type
      this.form.description = this.pageList[row.id].description
      this.diagLogVisible = true
    },
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
