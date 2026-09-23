package com.huabo.etl.utils;

import com.huabo.etl.repo.XRepoManager;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleSecurityException;
import org.pentaho.di.repository.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KettleUtil {
    private static final Logger log = LoggerFactory.getLogger(KettleUtil.class);
    public static Map<String, Repository> holder = new ConcurrentHashMap<String, Repository>();

    public static Map<String, Repository> getHolder() {
        return holder;
    }

    public static void setHolder(Map<String, Repository> holder) {
        KettleUtil.holder = holder;
    }

    public KettleUtil() {
    }

    public static Repository conFileRep(String repoId, String repoName, String baseDirectory) throws KettleException {
        if (holder.containsKey(repoId)) {
            return (Repository) holder.get(repoId);
        } else {
            XRepoManager.createFileRep(repoId, repoName, "文件资源库", baseDirectory);
            Repository repository = connect(repoId);
            if (null != repository) {
                holder.put(repoId, repository);
                return repository;
            } else {
                return null;
            }
        }
    }

    public static Repository connect(String repoId) throws KettleSecurityException, KettleException {
        return connect(repoId, (String) null, (String) null);
    }

    public static Repository connect(String repoId, String username, String password) throws KettleSecurityException, KettleException {
        Repository repository = (Repository) XRepoManager.repositoryCache.get(repoId);
        if (!repository.isConnected()) {
            repository.connect(username, password);
            log.info(repository.getName() + "资源库已连接!");
        }

        return repository;
    }
    /*public static Repository conByJndi(String repoId, String name, String db, String type, String kuser, String kpass) throws Exception {
        testConnect(repoId);
        if (holder.containsKey(repoId)) {
            return (Repository)holder.get(repoId);
        } else {
            XRepoManager.createDBRepByJndi(repoId, name, type, db);
            Repository repository = connect(repoId, kuser, kpass);
            if (null != repository) {
                holder.put(repoId, repository);
                return repository;
            } else {
                return null;
            }
        }
    }



    public static Repository conByNative(String repoId, String repoName, String name, String type, String host, String port, String db, String user, String pass, String kuser, String kpass) throws KettleException {
        testConnect(repoId);
        if (holder.containsKey(repoId)) {
            return (Repository)holder.get(repoId);
        } else {
            XRepoManager.createDBRepByDesc(repoId, repoName, "资源库", name, type, "Native", host, db, port, user, pass);
            Repository repository = connect(repoId, kuser, kpass);
            if (null != repository) {
                holder.put(repoId, repository);
                return repository;
            } else {
                return null;
            }
        }
    }

    public static DatabaseMeta createDatabaseMeta(String name, String type, String access, String host, String dbName, String port, String user, String pass, boolean replace, Repository repository) {
        return XRepoManager.createDatabaseMeta(name, type, access, host, dbName, port, user, pass, (Map)null, replace, repository);
    }

    public static DatabaseMeta createDatabaseMeta(String name, String type, String access, String host, String dbName, String port, String user, String pass, Map params, Repository repository) {
        return XRepoManager.createDatabaseMeta(name, type, access, host, dbName, port, user, pass, params, false, repository);
    }

    public static Repository conByParams(String repoId, String repoName, JSONObject params, String name, String type, String access, String host, String db, String port, String user, String pass, String kuser, String kpass) throws Exception {
        testConnect(repoId);
        if (holder.containsKey(repoId)) {
            return (Repository)holder.get(repoId);
        } else {
            XRepoManager.createDBRepByParams(repoId, repoName, params, name, type, access, host, db, port, user, pass);
            Repository repository = connect(repoId, kuser, kpass);
            if (null != repository) {
                holder.put(repoId, repository);
                return repository;
            } else {
                return null;
            }
        }
    }



    public static void setRepository(Repository repository) {
    }

    public static void destroy(String id) {
        XRepoManager.disConnectionRepository(id);
    }

    public static void delJob(String repoId, long id_job) throws KettleException {
        testConnect(repoId);
        delJob(id_job, (Repository)holder.get(repoId));
    }

    private static String calcRelativeElementDirectory(RepositoryDirectoryInterface dir) {
        return dir != null ? dir.getPath() : "/";
    }

    private static String calcDirectoryName(String repoId, RepositoryDirectoryInterface dir) {
        testConnect(repoId);
        StringBuilder directory = new StringBuilder();
        KettleFileRepositoryMeta repositoryMeta = (KettleFileRepositoryMeta)((Repository)holder.get(repoId)).getRepositoryMeta();
        String baseDir = repositoryMeta.getBaseDirectory();
        baseDir = Const.replace(baseDir, "\\", "/");
        directory.append(baseDir);
        if (!baseDir.endsWith("/")) {
            directory.append("/");
        }

        if (dir != null) {
            String path = calcRelativeElementDirectory(dir);
            if (path.startsWith("/")) {
                directory.append(path.substring(1));
            } else {
                directory.append(path);
            }

            if (!path.endsWith("/")) {
                directory.append("/");
            }
        }

        return directory.toString();
    }

    public static void delFileJob(String repoId, String jobId) throws KettleException {
        String path = jobId.substring(0, jobId.lastIndexOf("/"));
        String jobName = jobId.substring(jobId.lastIndexOf("/") + 1);
        RepositoryDirectoryInterface repositoryDirectoryInterface = ((Repository)holder.get(repoId)).findDirectory(path);
        String filename = calcFilename(repoId, repositoryDirectoryInterface, jobName);
        deleteFile(filename);
    }

    private static String calcFilename(String repoId, RepositoryDirectoryInterface repositoryDirectoryInterface, String jobName) {
        return calcDirectoryName(repoId, (RepositoryDirectoryInterface)null) + repositoryDirectoryInterface.getPath() + "/" + jobName;
    }

    private static void deleteFile(String filename) throws KettleException {
        try {
            FileObject fileObject = KettleVFS.getFileObject(filename);
            fileObject.delete();
        } catch (Exception var2) {
            throw new KettleException("Unable to delete file with name [" + filename + "]", var2);
        }
    }

    public static void delJob(long id_job, Repository repository) throws KettleException {
        repository.deleteJob(new LongObjectId(id_job));
    }

    public static void delTrans(String repoId, long id_job) throws KettleException {
        testConnect(repoId);
        delTrans(id_job, (Repository)holder.get(repoId));
    }

    public static void delTrans(long id_job, Repository repository) throws KettleException {
        repository.deleteTransformation(new LongObjectId(id_job));
    }

    public static JobMeta loadJob(String repoId, long jobId) throws KettleException {
        testConnect(repoId);
        return ((Repository)holder.get(repoId)).loadJob(new LongObjectId(jobId), (String)null);
    }

    public static JobMeta loadJob(String repoId, String jobId) throws KettleException {
        testConnect(repoId);
        return ((Repository)holder.get(repoId)).loadJob(new StringObjectId(jobId), (String)null);
    }

    public static JobMeta loadJob(String repoId, String jobname, String directory) throws KettleException {
        testConnect(repoId);
        return loadJob(jobname, directory, (Repository)holder.get(repoId));
    }

    public static JobMeta loadJob(String jobname, String directory, Repository repository) throws KettleException {
        RepositoryDirectoryInterface dir = repository.findDirectory(directory);
        return repository.loadJob(jobname, dir, (ProgressMonitorListener)null, (String)null);
    }

    public static JobMeta loadJob(String repoId, String jobname, long directory) throws KettleException {
        testConnect(repoId);
        return loadJob(jobname, directory, (Repository)holder.get(repoId));
    }

    public static JobMeta loadJob(String jobname, long directory, Repository repository) throws KettleException {
        RepositoryDirectoryInterface dir = repository.findDirectory(new LongObjectId(directory));
        return repository.loadJob(jobname, dir, (ProgressMonitorListener)null, (String)null);
    }

    public static TransMeta loadTrans(String repoId, long id) throws KettleException {
        testConnect(repoId);
        return ((Repository)holder.get(repoId)).loadTransformation(new LongObjectId(id), (String)null);
    }

    public static TransMeta loadTrans(String repoId, String transname, String directory) throws KettleException {
        testConnect(repoId);
        return loadTrans(transname, directory, (Repository)holder.get(repoId));
    }

    public static TransMeta loadTrans(String transname, String directory, Repository repository) throws KettleException {
        RepositoryDirectoryInterface dir = repository.findDirectory(directory);
        return repository.loadTransformation(transname, dir, (ProgressMonitorListener)null, true, (String)null);
    }

    public static TransMeta loadTrans(String repoId, JobMeta jobMeta, String teansName) throws KettleException {
        JobEntryTrans trans = (JobEntryTrans)jobMeta.findJobEntry(teansName).getEntry();
        TransMeta transMeta = loadTrans(repoId, trans.getTransname(), trans.getDirectory());
        return transMeta;
    }

    public static <T extends JobEntryBase> T loadJobEntry(String repoId, JobMeta jobMeta, String jobEntryName, T jobEntryMeta) throws KettleException {
        return loadJobEntry(repoId, jobMeta.findJobEntry(jobEntryName).getEntry().getObjectId(), jobEntryMeta);
    }

    public static <T extends JobEntryBase> T loadJobEntry(String repoId, ObjectId entryId, T jobEntryMeta) throws KettleException {
        testConnect(repoId);
        jobEntryMeta.loadRep((Repository)holder.get(repoId), (IMetaStore)null, entryId, (List)null, (List)null);
        jobEntryMeta.setObjectId(entryId);
        return jobEntryMeta;
    }

    public static JobEntrySpecial findStart(JobMeta jobMeta) {
        for(int i = 0; i < jobMeta.nrJobEntries(); ++i) {
            JobEntryCopy jec = jobMeta.getJobEntry(i);
            JobEntryInterface je = jec.getEntry();
            if (je.getPluginId().equals("SPECIAL")) {
                return (JobEntrySpecial)je;
            }
        }

        return null;
    }

    public static void saveRepositoryElement(String repoId, RepositoryElementInterface repositoryElement) throws KettleException {
        testConnect(repoId);
        saveRepositoryElement((Repository)holder.get(repoId), repositoryElement);
    }

    public static void saveRepositoryElement(Repository repository, RepositoryElementInterface repositoryElement) throws KettleException {
        repository.save(repositoryElement, (String)null, (ProgressMonitorListener)null, true);
    }

    public static void saveTrans(String repoId, TransMeta transMeta) throws KettleException {
        testConnect(repoId);
        saveRepositoryElement((Repository)((Repository)holder.get(repoId)), transMeta);
    }

    public static void saveTrans(Repository repository, TransMeta transMeta) throws KettleException {
        saveRepositoryElement((Repository)repository, transMeta);
    }

    public static void saveJob(String repoId, JobMeta jobMeta) throws KettleException {
        testConnect(repoId);
        saveRepositoryElement((Repository)((Repository)holder.get(repoId)), jobMeta);
    }

    public static void saveJob(Repository repository, JobMeta jobMeta) throws KettleException {
        saveRepositoryElement((Repository)repository, jobMeta);
    }

    public static boolean isDirectoryExist(Repository repository, String directoryName) {
        try {
            RepositoryDirectoryInterface dir = repository.findDirectory(directoryName);
            return dir != null;
        } catch (KettleException var3) {
            log.error("判断job目录是否存在失败！", var3);
            return false;
        }
    }

    public static RepositoryDirectoryInterface getOrMakeDirectory(String repoId, String parentDirectory, String directoryName) throws KettleException {
        testConnect(repoId);
        RepositoryDirectoryInterface parent = ((Repository)holder.get(repoId)).findDirectory(parentDirectory);
        if (StringUtils.isBlank(parentDirectory)) {
            parent = ((Repository)holder.get(repoId)).findDirectory("/");
        }

        if (StringUtils.isNotBlank(directoryName)) {
            RepositoryDirectoryInterface dir = ((Repository)holder.get(repoId)).findDirectory(parentDirectory + "/" + directoryName);
            return dir == null ? ((Repository)holder.get(repoId)).createRepositoryDirectory(parent, directoryName) : dir;
        } else {
            return parent;
        }
    }

    public static RepositoryDirectoryInterface makeDirs(String repoId, String directoryName) throws KettleException {
        if (!StringUtils.isNotBlank(directoryName)) {
            return null;
        } else {
            String parentDirectory = "";
            String[] dirArr = directoryName.replace("\\", "/").split("/");
            String[] var6 = dirArr;
            int var5 = dirArr.length;

            for(int var4 = 0; var4 < var5; ++var4) {
                String dirStr = var6[var4];

                try {
                    if (StringUtils.isNotBlank(dirStr)) {
                        RepositoryDirectoryInterface p = getOrMakeDirectory(repoId, parentDirectory, dirStr);
                        parentDirectory = p.getPath();
                    }
                } catch (Exception var9) {
                    log.error("创建目录失败：" + directoryName + "," + parentDirectory + "," + dirStr, var9);
                }
            }

            return getOrMakeDirectory(repoId, parentDirectory, (String)null);
        }
    }

    public static String getDirectory(String repoId, long dirId) throws KettleException {
        return getDirectory(repoId, new LongObjectId(dirId));
    }

    public static String getDirectory(String repoId, ObjectId dirId) throws KettleException {
        testConnect(repoId);
        RepositoryDirectoryInterface dir = ((Repository)holder.get(repoId)).findDirectory(dirId);
        return dir == null ? null : dir.getPath();
    }

    public static void setStepToTrans(TransMeta teans, String stepName, StepMetaInterface smi) {
        StepMeta step = teans.findStep(stepName);
        step.setStepMetaInterface(smi);
    }

    public static void setStepToTransAndSave(String repoId, TransMeta teans, String stepName, StepMetaInterface smi) throws KettleException {
        setStepToTrans(teans, stepName, smi);
        saveTrans(repoId, teans);
    }

    public static void jobStopAll(Job job) {
        job.stopAll();
        JobMeta jobMeta = job.getJobMeta();
        Iterator var3 = jobMeta.getJobCopies().iterator();

        while(var3.hasNext()) {
            JobEntryCopy jec = (JobEntryCopy)var3.next();
            if (jec.isTransformation()) {
                JobEntryTrans jet = (JobEntryTrans)jec.getEntry();
                if (jet.getTrans() != null) {
                    jet.getTrans().stopAll();
                }
            } else if (jec.isJob()) {
                JobEntryJob jej = (JobEntryJob)jec.getEntry();
                if (jej.getJob() != null) {
                    jobStopAll(jej.getJob());
                }
            }
        }

    }

    public static void jobKillAll(Job job) {
        job.stopAll();
        JobMeta jobMeta = job.getJobMeta();
        Iterator var3 = jobMeta.getJobCopies().iterator();

        while(var3.hasNext()) {
            JobEntryCopy jec = (JobEntryCopy)var3.next();
            if (jec.isTransformation()) {
                JobEntryTrans jet = (JobEntryTrans)jec.getEntry();
                if (jet.getTrans() != null) {
                    jet.getTrans().killAll();
                }
            } else if (jec.isJob()) {
                JobEntryJob jej = (JobEntryJob)jec.getEntry();
                if (jej.getJob() != null) {
                    jobKillAll(jej.getJob());
                }
            }
        }

        if (!job.getState().equals(Thread.State.BLOCKED) && !job.getState().equals(Thread.State.TIMED_WAITING)) {
            job.interrupt();
        } else {
            job.stop();
        }

    }

    public static void transCopy(Map param, Repository fromRepository, Repository toRepository) throws KettleException {
        String jobName = param.get("transName") + "";
        String jobPath = param.get("transPath") + "";
        String newName = param.get("newName") + "";
        String newPath = param.get("newPath") + "";
        String des = param.get("transDescription") + "";
        TransMeta jobMeta = loadTrans(jobName, jobPath, fromRepository);
        new TransMeta();
        jobMeta.setName(newName);
        jobMeta.setDescription(des);
        jobMeta.setRepositoryDirectory(makeDirs(param.get("repoId") + "", newPath));
        saveTrans(toRepository, jobMeta);
    }

    public static void jobCopy(Map param, Repository fromRepository, Repository toRepository) throws KettleException {
        String jobName = param.get("jobName") + "";
        String jobPath = param.get("jobPath") + "";
        String newName = param.get("newName") + "";
        String newPath = param.get("newPath") + "";
        String des = param.get("jobDescription") + "";
        JobMeta jobMeta = loadJob(jobName, jobPath, fromRepository);
        jobMeta.setName(newName);
        jobMeta.setDescription(des);
        jobMeta.setRepositoryDirectory(makeDirs(param.get("repoId") + "", newPath));
        saveJob(toRepository, jobMeta);
    }

    public static void jobCopy(String jobName, String jobPath, Repository fromRepository, Repository toRepository) throws KettleException {
        JobMeta jobMeta = loadJob(jobName, jobPath, fromRepository);
        Iterator var6 = jobMeta.getJobCopies().iterator();

        while(var6.hasNext()) {
            JobEntryCopy jec = (JobEntryCopy)var6.next();
            if (jec.isTransformation()) {
                JobEntryTrans jet = (JobEntryTrans)jec.getEntry();
                transCopy(jet.getObjectName(), jet.getDirectory(), fromRepository, toRepository);
            } else if (jec.isJob()) {
                JobEntryJob jej = (JobEntryJob)jec.getEntry();
                jobCopy(jej.getObjectName(), jej.getDirectory(), fromRepository, toRepository);
            }
        }

        jobMeta.setRepository(toRepository);
        jobMeta.setMetaStore(toRepository.getMetaStore());
        if (!isDirectoryExist(toRepository, jobPath)) {
            toRepository.createRepositoryDirectory(toRepository.findDirectory("/"), jobPath);
        }

        saveJob(toRepository, jobMeta);
    }

    public static void transCopy(String transName, String transPath, Repository fromRepository, Repository toRepository) throws KettleException {
        TransMeta tm = loadTrans(transName, transPath, fromRepository);
        Iterator var6 = tm.getSteps().iterator();

        while(var6.hasNext()) {
            StepMeta sm = (StepMeta)var6.next();
            if (sm.isJobExecutor()) {
                JobExecutorMeta jem = (JobExecutorMeta)sm.getStepMetaInterface();
                jobCopy(jem.getJobName(), jem.getDirectoryPath(), fromRepository, toRepository);
            } else if (sm.getStepMetaInterface() instanceof TransExecutorMeta) {
                TransExecutorMeta te = (TransExecutorMeta)sm.getStepMetaInterface();
                transCopy(te.getTransName(), te.getDirectoryPath(), fromRepository, toRepository);
            }
        }

        if (!isDirectoryExist(toRepository, transPath)) {
            toRepository.createRepositoryDirectory(toRepository.findDirectory("/"), transPath);
        }

        tm.setRepository(toRepository);
        tm.setMetaStore(toRepository.getMetaStore());
        saveTrans(toRepository, tm);
    }

    public static ObjectId getJobId(String repoId, JobMeta jm) {
        return getJobId(repoId, jm.getName(), jm.getRepositoryDirectory());
    }

    public static ObjectId getJobId(String repoId, String name, RepositoryDirectoryInterface repositoryDirectory) {
        try {
            testConnect(repoId);
            return ((Repository)holder.get(repoId)).getJobId(name, repositoryDirectory);
        } catch (KettleException var4) {
            log.info("获取作业id失败", var4);
            return null;
        }
    }

    public static ObjectId getTransformationID(String repoId, TransMeta tm) {
        return getTransformationID(repoId, tm.getName(), tm.getRepositoryDirectory());
    }

    public static ObjectId getTransformationID(String repoId, String name, RepositoryDirectoryInterface repositoryDirectory) {
        try {
            testConnect(repoId);
            return ((Repository)holder.get(repoId)).getTransformationID(name, repositoryDirectory);
        } catch (KettleException var4) {
            log.info("获取转换id失败", var4);
            return null;
        }
    }

    public static void repairTransHop(TransMeta tm) {
        for(int i = 0; i < tm.nrTransHops(); ++i) {
            TransHopMeta hop = tm.getTransHop(i);
            hop.setFromStep(tm.findStep(hop.getFromStep().getName()));
            hop.setToStep(tm.findStep(hop.getToStep().getName()));
        }

    }

    public static void setParams(NamedParams target, NamedParams source, Map<String, String> params) {
        target.eraseParameters();

        try {
            String[] var6;
            int var5 = (var6 = source.listParameters()).length;

            for(int var4 = 0; var4 < var5; ++var4) {
                String key = var6[var4];
                String defaultVal = source.getParameterDefault(key);
                if (params.containsKey(key)) {
                    defaultVal = (String)params.get(key);
                }

                target.addParameterDefinition(key, defaultVal, source.getParameterDescription(key));
            }
        } catch (Exception var8) {
            log.error("保存JOB失败", var8);
        }

    }

    public static void repairHop(JobMeta jm) {
        Iterator var2 = jm.getJobhops().iterator();

        while(var2.hasNext()) {
            JobHopMeta hop = (JobHopMeta)var2.next();
            hop.setFromEntry(jm.findJobEntry(hop.getFromEntry().getName()));
            hop.setToEntry(jm.findJobEntry(hop.getToEntry().getName()));
        }

    }

    public static String getProp(VariableSpace vs, String key) {
        String value = vs.environmentSubstitute("${" + key + "}");
        return value.startsWith("${") ? "" : value;
    }

    public static JSONObject getPropJSONObject(VariableSpace vs, String key) {
        String value = getProp(vs, key);
        return StringUtils.isNotBlank(value) ? JSON.parseObject(value) : null;
    }

    public static Job getRootJob(Job rootjob) {
        while(rootjob != null && rootjob.getParentJob() != null) {
            rootjob = rootjob.getParentJob();
        }

        return rootjob;
    }

    public static Job getRootJob(JobEntryBase jee) {
        Job rootjob = jee.getParentJob();
        return getRootJob(rootjob);
    }

    public static Job getRootJob(StepInterface si) {
        Job rootjob = si.getTrans().getParentJob();
        return getRootJob(rootjob);
    }

    public static String getRootJobId(JobEntryBase jee) {
        return getRootJob(jee).getObjectId().getId();
    }

    public static String getRootJobId(StepInterface si) {
        Job rootjob = getRootJob(si);
        return rootjob != null ? rootjob.getObjectId().getId() : null;
    }

    public static String getRootJobName(StepInterface si) {
        Job rootjob = getRootJob(si);
        return rootjob != null ? rootjob.getObjectName() : null;
    }

    public static BufferedImage generateTransformationImage(TransMeta transMeta) throws Exception {
        float magnification = 1.0F;
        Point maximum = transMeta.getMaximum();
        maximum.multiply(magnification);
        SwingGC gc = new SwingGC((ImageObserver)null, maximum, 32, 0, 0);
        TransPainter transPainter = new TransPainter(gc, transMeta, maximum, (ScrollBarInterface)null, (ScrollBarInterface)null, (TransHopMeta)null, (Point)null, (Rectangle)null, new ArrayList(), new ArrayList(), 32, 1, 0, 0, true, "Arial", 10);
        transPainter.setMagnification(magnification);
        transPainter.buildTransformationImage();
        BufferedImage image = (BufferedImage)gc.getImage();
        return image;
    }

    public static BufferedImage generateJobImage(JobMeta jobMeta) throws Exception {
        float magnification = 1.0F;
        Point maximum = jobMeta.getMaximum();
        maximum.multiply(magnification);
        SwingGC gc = new SwingGC((ImageObserver)null, maximum, 32, 0, 0);
        JobPainter jobPainter = new JobPainter(gc, jobMeta, maximum, (ScrollBarInterface)null, (ScrollBarInterface)null, (JobHopMeta)null, (Point)null, (Rectangle)null, new ArrayList(), new ArrayList(), 32, 1, 0, 0, true, "Arial", 10);
        jobPainter.setMagnification(magnification);
        jobPainter.drawJob();
        BufferedImage image = (BufferedImage)gc.getImage();
        return image;
    }

    public static XJobStatus getJobStatus(Job job) {
        String status = job.getStatus();
        if (status.indexOf("errors") > -1) {
            return XJobStatus.FAILED;
        } else if (status.equals("Waiting")) {
            return XJobStatus.PENDING;
        } else if (status.equals("Halting")) {
            return XJobStatus.HALTING;
        } else if (status.equals("Running")) {
            return XJobStatus.RUNNING;
        } else if (status.equals("Stopped")) {
            return XJobStatus.STOPPED;
        } else {
            return status.equalsIgnoreCase("Finished") ? XJobStatus.FINISHED : XJobStatus.UNKNOWN;
        }
    }

    public static XTransStatus getTransStatus(Trans trans) {
        String status = trans.getStatus();
        if (status.indexOf("errors") > -1) {
            return XTransStatus.FAILED;
        } else if (status.equals("Waiting")) {
            return XTransStatus.WAITING;
        } else if (status.equals("Halting")) {
            return XTransStatus.HALTING;
        } else if (status.equals("Running")) {
            return XTransStatus.RUNNING;
        } else if (status.equals("Stopped")) {
            return XTransStatus.STOPPED;
        } else if (status.equals("Finished")) {
            return XTransStatus.FINISHED;
        } else if (status.equals("Paused")) {
            return XTransStatus.PAUSED;
        } else if (status.contains("Preparing")) {
            return XTransStatus.PREPARING;
        } else {
            return status.equals("Initializing") ? XTransStatus.INITIALIZING : XTransStatus.UNKNOWN;
        }
    }

    private static void testConnect(String repoId) {
        Repository repository = (Repository)holder.get(repoId);
        if (repository instanceof KettleDatabaseRepository) {
            KettleDatabaseRepository kettleDatabaseRepository = (KettleDatabaseRepository)repository;
            Database database = kettleDatabaseRepository.getDatabase();
            Connection connection = database.getConnection();

            try {
                if (connection.isClosed()) {
                    holder.remove(repoId);
                    log.info("当前数据库连接已经关闭,准备重新连接.......");
                    DatabaseMeta databaseMeta = (DatabaseMeta)XRepoManager.databaseMeta.get("databaseMeta");
                    XRepoManager.createDBRepByDesc(repoId, repository.getName(), "资源库", databaseMeta.getName(), databaseMeta.getDatabaseTypeDesc(), "Native", databaseMeta.getHostname(), databaseMeta.getDatabaseName(), databaseMeta.getDatabasePortNumberString(), databaseMeta.getUsername(), databaseMeta.getPassword());
                    kettleDatabaseRepository = (KettleDatabaseRepository)connect(repoId, "admin", "admin");
                    if (null != repository) {
                        holder.put(repoId, kettleDatabaseRepository);
                    }
                }
            } catch (SQLException var6) {
                log.error("数据库重连出现异常,{}", var6);
            } catch (KettleException var7) {
                log.error("数据库重连出现异常,{}", var7);
            }
        }

    }*/
}
