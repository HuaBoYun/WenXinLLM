package com.landray.kmss.sys.notify.webservice;

public class ISysNotifyTodoWebServiceProxy implements ISysNotifyTodoWebService {
  private String _endpoint = null;
  private ISysNotifyTodoWebService iSysNotifyTodoWebService = null;
  
  public ISysNotifyTodoWebServiceProxy() {
    _initISysNotifyTodoWebServiceProxy();
  }
  
  public ISysNotifyTodoWebServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initISysNotifyTodoWebServiceProxy();
  }
  
  private void _initISysNotifyTodoWebServiceProxy() {
    try {
      iSysNotifyTodoWebService = (new ISysNotifyTodoWebServiceServiceLocator()).getISysNotifyTodoWebServicePort();
      if (iSysNotifyTodoWebService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iSysNotifyTodoWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iSysNotifyTodoWebService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iSysNotifyTodoWebService != null)
      ((javax.xml.rpc.Stub)iSysNotifyTodoWebService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public ISysNotifyTodoWebService getISysNotifyTodoWebService() {
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService;
  }
  
  public NotifyTodoAppResult updateTodo(NotifyTodoUpdateContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.updateTodo(arg0);
  }
  
  public NotifyTodoAppResult setTodoDone(NotifyTodoRemoveContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.setTodoDone(arg0);
  }
  
  public NotifyTodoAppResult getTodo(NotifyTodoGetContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.getTodo(arg0);
  }
  
  public NotifyTodoAppResult getTodoCount(NotifyTodoGetCountContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.getTodoCount(arg0);
  }
  
  public NotifyTodoAppResult deleteTodo(NotifyTodoRemoveContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.deleteTodo(arg0);
  }
  
  public NotifyTodoAppResult sendTodo(NotifyTodoSendContext arg0) throws java.rmi.RemoteException, Exception{
    if (iSysNotifyTodoWebService == null)
      _initISysNotifyTodoWebServiceProxy();
    return iSysNotifyTodoWebService.sendTodo(arg0);
  }
  
  
}