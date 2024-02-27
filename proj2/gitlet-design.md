# Gitlet Design Document

**Name**:Yang Dong

## Classes and Data Structures


### FileStructure
- .gitlet
   - blobs
   - pointers
   - commits


        

### Class:Commit 
#### 描述：

#### Instance variable
* private String message - 提交时候的信息
* private Date commitDate - 提交日期
* private String parent - 父引用的名字
* private String Commit_FOLDER - Commit 序列化存储的文件夹
* private StagingArea StoreArea - 暂存区域——文件名与其对应的文件版本

#### Instance Method
* public void SetStagingArea(StagingArea StoreArea) - 修改将要commit的commit的暂存区域
*  public static void CommitWrite(Commit TheCommit, String FileName) - 包装一个Commit存储到本地的方法

#### Static Method
* public static void CommitWrite() - 将Commit序列化写入文件夹


### Class:InitMethod
* private String master - 当前分支指针
* 
#### Static Method
 * static void InitFileFolder()  - 初始化文件目录 

### Class:Pointer 
* private String CurrentLocation - 存储当前指针所指向commit的哈希值

### Class:StagingArea
#### 描述：该类用于暂存 文件名与其对应的文件版本
* public TreeMap<String, String> StoreArea - 暂存区域


## Algorithms

## Persistence

