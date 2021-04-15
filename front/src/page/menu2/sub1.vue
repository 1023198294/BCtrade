<template lang="html">
  <div class="table-content">
    <el-table v-loading="loadingPitch" :data="pageList" size="mini" border style="width: 100%">
      <el-table-column prop="id" label=" " width="50"></el-table-column>
      <el-table-column prop="dataid" label="数据id" width="100"></el-table-column>
      <el-table-column prop="originalId" label="源数据id" width="100"></el-table-column>
      <el-table-column prop="creatorId" label="创建者id" width="140"></el-table-column>
      <el-table-column prop="createdDate" label="创建日期" width="240"></el-table-column>
      <el-table-column prop="value" label="价值"></el-table-column>
      <el-table-column fixed="right" label="操作" width="500">
        <template slot-scope="scope">
          <el-button @click="handleUpdateData(scope.row)" type="text" size="small">发布新数据版本</el-button>
          <el-button @click="handleCheckRow(scope.row)" type="text" size="small">详细</el-button>
          <el-button @click="handleDownloadRow(scope.row)" type="text" size="small">下载</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button size="small" type="primary" @click="dialogFormVisible = true; clearAll()" >上传个人数据</el-button>
    <el-dialog title="数据概览" :visible.sync="dialogCheckVisible">
      <div>
        <h2>数据名称</h2>
        <p>
          {{this.dataN}}
        </p>
        <h2>数据描述</h2>
        <p>
          {{this.dataD}}
        </p>
      </div>

    </el-dialog>
    <el-dialog title="数据信息" :visible.sync="dialogFormVisible">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="数据名称" prop="filename">
          <el-input v-model="form.filename" placeholder="请输入数据名"></el-input>
        </el-form-item>
        <el-form-item label="数据描述" prop="description">
          <el-input type="textarea" :rows="5" placeholder="请输入数据描述" v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item label="数据价值" prop="value">
          <el-input-number v-model="form.value" oninput="value=value.replace(/[^\d.]/g,'')"></el-input-number>
        </el-form-item>
      </el-form>

      <el-upload
        class="upload-demo"
        :action=posturl
        :data="{dataname:this.form.filename,size:this.form.size,value:this.form.value,description:this.form.description,dataRealName:this.form.dataRealName}"
        ref="upload"
        multiple
        :on-change="updateSize"
        :on-preview="handlePreview"
        :limit="1"
        :auto-upload="false"
        :file-list="filelist"
        :multiple="false"
        :on-exceed="removeFile"
        :on-remove="handleRemove"
        :on-success="handleRes"
        :with-credentials="true"
      >
        <el-button slot="trigger" size="small" type="primary">选择文件上传</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      </el-upload>
    </el-dialog>

    <el-dialog title="上传数据" :visible.sync="dialog2FormVisible2">
      <el-form :model="form" :rules="rules" ref="form">
        <el-form-item label="数据名称" prop="filename">
          <el-input v-model="form.filename" placeholder="请输入数据名"></el-input>
        </el-form-item>
        <el-form-item label="数据描述" prop="description">
          <el-input type="textarea" :rows="5" placeholder="请输入数据描述" v-model="form.description"></el-input>
        </el-form-item>
        <el-form-item label="数据价值" prop="value">
          <el-input-number v-model="form.value" ></el-input-number>
        </el-form-item>
        <el-form-item label="资产报酬率" prop="rate">
          <el-input-number v-model="form.rate" :min="5" :max="85" label="从修改版本中获利的比例"></el-input-number>%
        </el-form-item>
      </el-form>

      <el-upload
        class="upload-demo"
        :action=posturl2
        :data="{dataname:this.form.filename,size:this.form.size,value:this.form.value,description:this.form.description,dataRealName:this.form.dataRealName,creatorId:this.form.creatorId,rate:1-(this.form.rate)/100,originalId:this.form.originalId}"
        ref="upload"
        multiple
        :on-change="updateSize"
        :on-preview="handlePreview"
        :limit="1"
        :auto-upload="false"
        :file-list="filelist"
        :multiple="false"
        :on-exceed="removeFile"
        :on-remove="handleRemove"
        :on-success="handleRes"
        :with-credentials="true"
      >
        <el-button slot="trigger" size="small" type="primary">选择文件上传</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
      </el-upload>
    </el-dialog>
    <!--el-button size="small" type="primary" @click="handleDownload">
      点击下载
    </el-button-->
<!--    <el-button size="small" type="primary" @click="handleDownload">-->
<!--      点击下载-->
<!--    </el-button>-->
    <el-pagination
      small
      @current-change="handleCurrentChange"
      layout="prev, pager, next"
      :page-size="pageSize"
      :total="pageSize*pageCount"
    >
    </el-pagination>
  </div>

</template>

<script>
import pagination from "common/pagination";
import axios from "axios";
export default {
  data() {
    return {
      dtrn:'',
      pageCount:1,
      dataN:'加载中...',
      dataD:'加载中...',
      loadingPitch:false,
      pageList:[],
      curPage:1,
      pageSize:5,
      form:{
        filename:'',
        description:'',
        size:0.0,
        value:0.0,
        dataRealName:'',
        creatorId:'',
        rate:30,
        originalId:'',
      },
      posturl:this.$global.baseUrl+"/admin/upload",
      posturl2:this.$global.baseUrl+"/admin/upload2",
      totalPage: 300,
      filename:"美赛H奖.pdf",
      filelist:[],
      dialogFormVisible:false,
      dialog2FormVisible2:false,
      dialogCheckVisible:false,
      rules:{
        filename: [
          {required: true, message: '文件名不为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: '文件描述不为空', trigger: 'blur'}
        ]
      },

    };
  },
  created() {
    this.handleUpdatePage()
  },
  methods: {
    clearAll(){
      this.form.filelist = [];
      this.form.filename = '';
      this.form.description = '';
      this.form.value = 0;
      this.form.dataRealName = '';
      },
    handleUpdateData(row){
      this.clearAll()
      this.dialog2FormVisible2 = true
      this.form.originalId = this.pageList[row.id].originalId
      this.form.creatorId = this.pageList[row.id].creatorId
    },
    handleDownloadRow(row){
      console.log('download row:'+row)
      var dtid = this.pageList[row.id].originalId

      console.log('download dataid:'+dtid)
      this.$axios({
        method:'get',
        url:this.$global.baseUrl+'/admin/getRealName',
        params:{
          dataid:dtid
        }
      }).then((res)=>{
        this.dtrn = res.data
        this.$axios({
          method:'get',
          url:this.$global.baseUrl+'/admin/Download',
          params:{
            dataId:dtid
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
      })
    },
    handleCheckRow(row){
      var dtid = this.pageList[row.id].originalId
      this.dataN='加载中...'
      this.dataD='加载中...'
      this.$axios({
        method:'post',
        url:this.$global.baseUrl+'/admin/queryInfo',
        params:{
          dataId:dtid
        }
      }).then((res)=>{
        this.dataN = res.data.dataname
        this.dataD = res.data.description
      })
      this.dialogCheckVisible = true
    },
    handleCurrentChange(currentPage){
      this.currentPage = currentPage
      this.pageList = []
      this.handleUpdatePage()
    },
    handleUpdatePage(){
      var _this = this
      this.pageList = []
      this.$axios(
        {
          method:'post',
          url:_this.$global.baseUrl+'/admin/getOwnData',
          params:{
            page:this.curPage,
            pageSize:this.pageSize
          }
        }
      ).then((res)=>{
        //console.log(res)
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
              id:i,
              dataid:res.data[idx].dataid,
              creatorId:res.data[idx].creatorId,
              createdDate:res.data[idx].createdDate,
              originalId:res.data[idx].originalId,
              value:res.data[idx].value,

              /*
                    <el-table-column prop="id" label=" " width="50"></el-table-column>
      <el-table-column prop="dataid" label="数据ID" width="140"></el-table-column>
      <el-table-column prop="creatorId" label="创建者id" width="140"></el-table-column>
      <el-table-column prop="createdDate" label="创建日期" width="140"></el-table-column>
      <el-table-column prop="value" label="价值"></el-table-column>
          */
            }

          this.pageList.push(temp)
        }}
      )
    },
    updateSize(file,fileList){
      //console.log(file)
      this.form.size = file.size
      this.form.dataRealName = file.name

    },
    submitUpload(){

      this.$refs.form.validate(
        valid=>{
          if(valid){
            this.loadingPitch = true
            this.$refs.upload.submit();
            //console.log(this.filelist)

            this.$notify({
              title:'notification',
              message:'检查完毕，准备上传',
              type:'success'
            })
            this.timer = setTimeout(()=>{   //设置延迟执行
              console.log('ok');
            },1000);
            this.clearAll();
            this.dialogFormVisible = false;
            this.dialog2FormVisible2 = false;
          }else{
            this.$notify({
              title:'error',
              message:'上传失败,请检查输入是否完整',
              type:'error'
            })
            this.loadingPitch = true
          }
        }
      )

    },
    handlePreview(file){
      console.log(file)
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handleRes(res){
      //console.log(res)
      if(res==='上传成功'){
        this.$notify({
          title:'notification',
          message:'上传成功',
          type:'success'
        })
        this.handleUpdatePage()
      }else{
        this.$notify({
          title:'error',
          message:res,
          type:'error'
        })
      }
      this.loadingPitch = false
    },
    removeFile(){
      this.$notify({
        title:'warning',
        message:'只能上传一个文件（已清除旧文件）',
        type:'warning'
      })
      this.filelist = [];
    },
    receivePageSize(val) {
      console.log(val);
    },
    receiveCurrentPage(val) {
      console.log(val);
    },
    handleDownload(){
      this.$axios({
        url:this.$global.baseUrl + '/admin/testDownload',
        method:'get',
        params:{
          fileName:this.filename
        },
        responseType:'blob'
      }).then((res)=>{
        const blob = res.data
        const reader = new FileReader()
        reader.readAsDataURL(blob)
        reader.onload = (e) => {
          const a = document.createElement('a')
          a.download = this.filename
          a.href = e.target.result
          document.body.appendChild(a)
          a.click()
          document.body.removeChild(a)
        }
      }).catch((error)=>console.log(error))
    },
  },
  components: {
    pagination
  }
};
</script>

<style scoped lang="scss">
.table-content{
  margin-top: 20px;
  margin-bottom: 20px;
}
</style>
