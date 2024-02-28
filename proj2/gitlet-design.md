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
* private String sha1Name - 储存的文件名

#### Instance Method 
* public String SerializeStore() - 包装一个Commit存储到本地的方法，同时返回文件名既文件的sha1值

#### Static Method



### Class:InitMethod
* private static File Init_FOLDER - 初始化的文件夹
* 
#### Static Method
 * static void InitFileFolder()  - 初始化文件目录 



### Class:Pointer 
#### Instance variable
* private String CurrentLocation - 存储当前指针所指向commit的哈希值

#### Instance Methon
*  public String SerializeStore() - 序列化存储Pointer 并返回文件名
### Class:StagingArea
#### 描述：该类用于暂存 文件名与其对应的文件版本
* public TreeMap<String, String> StoreArea - 暂存区域 
* private static String Pointer_FOLDER = "pointers" - Pointer所存储的文件夹
* private String Pointer_Name - 指针名字


## Algorithms

## Persistence

