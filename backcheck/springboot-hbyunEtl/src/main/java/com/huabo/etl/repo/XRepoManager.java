package com.huabo.etl.repo;


import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.repository.*;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.pentaho.di.repository.kdr.KettleDatabaseRepositoryMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class XRepoManager {
    private static final Logger log = LoggerFactory.getLogger(XRepoManager.class);
    public static Map<String, Repository> repositoryCache = new HashMap<String, Repository>();
    public static Map<String, DatabaseMeta> databaseMeta = new ConcurrentHashMap<String, DatabaseMeta>();

    public XRepoManager() {
    }

    public static List<RepositoryTree> getAllDirectoryTreeList(String repoId, Repository repository, String path, List<RepositoryTree> allRepositoryTreeList, String type) throws KettleException {
        List<RepositoryTree> repositoryTreeList = getJobAndTrans(repoId, repository, path, type);
        if (repositoryTreeList.size() != 0) {
            Iterator var5 = repositoryTreeList.iterator();

            while (var5.hasNext()) {
                RepositoryTree repositoryTree = (RepositoryTree) var5.next();
                if (!repositoryTree.isLasted()) {
                    getAllDirectoryTreeList(repoId, repository, repositoryTree.getPath(), allRepositoryTreeList, type);
                    allRepositoryTreeList.add(repositoryTree);
                } else {
                    allRepositoryTreeList.add(repositoryTree);
                }
            }
        }

        return allRepositoryTreeList;
    }

    public static List<RepositoryTree> getJobAndTrans(String repoId, Repository repository, String path, String type) throws KettleException {
        RepositoryDirectoryInterface rDirectory = repository.loadRepositoryDirectoryTree().findDirectory(path);
        List<RepositoryTree> repositoryTreeList = getDirectory(repoId, repository, rDirectory);
        List<RepositoryElementMetaInterface> li = repository.getJobAndTransformationObjects(rDirectory.getObjectId(), false);
        if (null != li) {
            Iterator var6 = li.iterator();

            while (var6.hasNext()) {
                RepositoryElementMetaInterface repel = (RepositoryElementMetaInterface) var6.next();
                RepositoryTree repositoryTree;
                StringBuilder stringBuilder;
                if ("job".equals(repel.getObjectType().toString()) && type.equals("job")) {
                    repositoryTree = new RepositoryTree();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("job").append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(repoId + "@" + rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    if (repository instanceof KettleDatabaseRepository) {
                        repositoryTree.setType(repoId + "@db@" + "job");
                    } else if (repository instanceof KettleFileRepository) {
                        repositoryTree.setType(repoId + "@file@" + "job");
                    }

                    repositoryTree.setLasted(true);
                    repositoryTree.setPath(repel.getRepositoryDirectory().getPath());
                    repositoryTreeList.add(repositoryTree);
                } else if ("transformation".equals(repel.getObjectType().toString()) && type.equals("trans")) {
                    repositoryTree = new RepositoryTree();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("transformation").append(rDirectory.getObjectId().toString()).append("@").append(repel.getObjectId().toString());
                    repositoryTree.setId(stringBuilder.toString());
                    repositoryTree.setParent(repoId + "@" + rDirectory.getObjectId().toString());
                    repositoryTree.setText(repel.getName());
                    if (repository instanceof KettleDatabaseRepository) {
                        repositoryTree.setType(repoId + "@db@" + "transformation");
                    } else if (repository instanceof KettleFileRepository) {
                        repositoryTree.setType(repoId + "@file@" + "transformation");
                    }

                    repositoryTree.setLasted(true);
                    repositoryTree.setPath(repel.getRepositoryDirectory().getPath());
                    repositoryTreeList.add(repositoryTree);
                }
            }
        }

        return repositoryTreeList;
    }

    private static List<RepositoryTree> getDirectory(String repoId, Repository repository, RepositoryDirectoryInterface rDirectory) throws KettleException {
        List<RepositoryTree> repositoryTreeList = new ArrayList<RepositoryTree>();
        if (null != repository && null != rDirectory) {
            RepositoryDirectoryInterface tree = repository.loadRepositoryDirectoryTree().findDirectory(rDirectory.getObjectId());
            if (rDirectory.getNrSubdirectories() > 0) {
                for (int i = 0; i < rDirectory.getNrSubdirectories(); ++i) {
                    RepositoryDirectory subTree = tree.getSubdirectory(i);
                    RepositoryTree repositoryTree = new RepositoryTree();
                    repositoryTree.setId(repoId + "@" + subTree.getObjectId().toString());
                    repositoryTree.setParent(repoId + "@" + rDirectory.getObjectId().toString());
                    repositoryTree.setText(subTree.getName());
                    repositoryTree.setPath(subTree.getPath());
                    repositoryTree.setType("subTree");
                    List<RepositoryElementMetaInterface> RepositoryElementMetaInterfaceList = repository.getJobAndTransformationObjects(subTree.getObjectId(), false);
                    if (subTree.getNrSubdirectories() <= 0 && RepositoryElementMetaInterfaceList.size() <= 0) {
                        repositoryTree.setLasted(true);
                    } else {
                        repositoryTree.setLasted(false);
                    }

                    repositoryTreeList.add(repositoryTree);
                }
            }
        }

        return repositoryTreeList;
    }

    public static Repository createRep(BaseRepositoryMeta baseRepositoryMeta, String id) throws KettleException {
        if (get(id) != null) {
            return get(id);
        } else {
            Repository repo = null;
            if (baseRepositoryMeta instanceof KettleDatabaseRepositoryMeta) {
                repo = new KettleDatabaseRepository();
                ((Repository) repo).init((KettleDatabaseRepositoryMeta) baseRepositoryMeta);
            } else {
                repo = new KettleFileRepository();
                ((Repository) repo).init((KettleFileRepositoryMeta) baseRepositoryMeta);
            }

            repositoryCache.put(id, repo);
            log.info(((Repository) repo).getName() + "资源库初始化成功");
            return (Repository) repo;
        }
    }

    public static Repository createFileRep(String repoId, String repName, String description, String baseDirectory) throws KettleException {
        if (!KettleEnvironment.isInitialized()) {
            KettleEnvironment.init();
        }

        KettleFileRepositoryMeta fileRepMeta = new KettleFileRepositoryMeta(repoId, repName, description, baseDirectory);
        return createRep(fileRepMeta, repoId);
    }

    private static Repository get(String repoId) {
        return (Repository) repositoryCache.get(repoId);
    }
   /* public static String[] getDataBaseAccess() {
        String[] dataBaseAccess = DatabaseMeta.dbAccessTypeCode;
        return dataBaseAccess;
    }

    public static DatabaseMeta createDatabaseMeta(String name, String type, String access, String host, String db, String port, String user, String pass, Map params, boolean replace, Repository repository) {
        DatabaseMeta dm = null;
        if (repository != null) {
            try {
                ObjectId dbId = repository.getDatabaseID(name);
                if (dbId != null && !replace) {
                    dm = repository.loadDatabaseMeta(dbId, (String)null);
                } else if (dbId != null && replace) {
                    repository.deleteDatabaseMeta(name);
                }
            } catch (KettleException var15) {
                log.error("创建数据库元数据失败", var15);
            }
        }

        if (dm == null) {
            dm = new DatabaseMeta(name, type, access, host, db, port, user, pass);
            if (params != null) {
                Iterator var13 = params.entrySet().iterator();

                while(var13.hasNext()) {
                    Map.Entry<String, Object> ent = (Map.Entry)var13.next();
                    dm.addExtraOption(type, (String)ent.getKey(), "" + ent.getValue());
                }
            }

            dm.setForcingIdentifiersToLowerCase(true);
            if (repository != null) {
                try {
                    repository.save(dm, (String)null, (ProgressMonitorListener)null, true);
                } catch (KettleException var14) {
                    log.error("保存数据库元数据失败", var14);
                }
            }
        }

        if (!StringUtils.isEmpty(dm)) {
            databaseMeta.put("databaseMeta", dm);
            log.info("第一次连接,获取到资源库数据库信息,{}", JSON.toJSON(dm));
        }

        return dm;
    }





    public static Repository createDBRepByJndi(String repoId, String name, String type, String db) throws KettleException {
        return createBaseMetaRep(repoId, (String)null, (String)null, (JSONObject)null, name, type, DatabaseMeta.dbAccessTypeCode[4], (String)null, db, (String)null, (String)null, (String)null);
    }

    public static Repository createDBRepByParams(String repoId, String repoName, JSONObject params, String databaseMetaName, String type, String access, String host, String db, String port, String user, String pass) throws KettleException {
        return createBaseMetaRep(repoId, repoName, repoName, params, databaseMetaName, type, access, host, db, port, user, pass);
    }

    public static Repository createDBRepByDesc(String repoId, String repoName, String description, String databaseMetaName, String type, String access, String host, String db, String port, String user, String pass) throws KettleException {
        return createBaseMetaRep(repoId, repoName, description, (JSONObject)null, databaseMetaName, type, access, host, db, port, user, pass);
    }

    public static Repository createBaseMetaRep(String repoId, String repoName, String description, JSONObject params, String databaseMetaName, String type, String access, String host, String db, String port, String user, String pass) throws KettleException {
        DatabaseMeta dataMeta = createDatabaseMeta(databaseMetaName, type, access, host, db, port, user, pass, params, false, (Repository)null);
        return createBaseRep(dataMeta, repoId, repoName, description);
    }

    public static Repository createBaseRep(DatabaseMeta dataMeta, String repoId, String repoName, String description) throws KettleException {
        KettleDatabaseRepositoryMeta kettleDatabaseMeta = new KettleDatabaseRepositoryMeta(repoId, repoName, description, dataMeta);
        return createRep(kettleDatabaseMeta, repoId);
    }

    public static KettleFileRepository createKFR(String id, String repName, String description, String baseDirectory) throws KettleException {
        KettleFileRepositoryMeta kettleFileRepositoryMeta = (KettleFileRepositoryMeta)createFileRep(id, repName, repName + "文件资源库", baseDirectory);
        KettleFileRepository kettleFileRepository = new KettleFileRepository();
        kettleFileRepository.init(kettleFileRepositoryMeta);
        return kettleFileRepository;
    }

    public static KettleDatabaseRepository createKDR(String name, String repoType, String dbAccess, String dbHost, String dbName, String dbPort, String dbUserName, String dbPass, String repoName, String repoId) throws KettleException {
        createDatabaseMeta(name, repoType, dbAccess, dbHost, dbName, dbPort, dbUserName, dbPass, (Map)null, false, (Repository)null);
        KettleDatabaseRepositoryMeta kettleDatabaseRepositoryMeta = (KettleDatabaseRepositoryMeta)createDBRepByDesc(repoId, repoName, repoName, name, repoType, dbAccess, dbHost, dbName, dbPort, dbUserName, dbPass);
        KettleDatabaseRepository kettleDatabaseRepository = new KettleDatabaseRepository();
        kettleDatabaseRepository.init(kettleDatabaseRepositoryMeta);
        return kettleDatabaseRepository;
    }

    public static void disConnectionRepository(String id) {
        if (repositoryCache.containsKey(id)) {
            Repository repository = (Repository)repositoryCache.get(id);
            repository.disconnect();
            repository.clearSharedObjectCache();
            repositoryCache.remove(id);
        }

    }

    public static void destroyAll() {
        repositoryCache.forEach((id, repository) -> {
            repository.disconnect();
            repository.clearSharedObjectCache();
        });
        repositoryCache.clear();
    }





    */
}
