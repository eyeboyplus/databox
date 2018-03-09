package cn.edu.fudan.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.core.runtime.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.PreferenceConstants;

import java.io.*;

public class DataboxNewWizard extends Wizard implements INewWizard {
	private DataboxNewWizardPage page;
	private ISelection selection;

	/**
	 * Constructor for SampleNewWizard.
	 */
	public DataboxNewWizard() {
		super();
		setNeedsProgressMonitor(true);
	}
	
	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		page = new DataboxNewWizardPage(selection);
		addPage(page);
	}

	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {
		System.out.println("enter function performFinish");
		// 获取工作区    
        IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();    
        String projectName = page.getProjectName();
        IProject project = workspaceRoot.getProject(projectName);
        if(project.exists()) {
        	page.setErrorMessage("The project " + projectName + " has existed.");
        	System.out.println("The project " + projectName + "has existed.");
        	return false;
        }
        try {
			project.create(null);
			project.open(null);
        } catch (CoreException e) {
			e.printStackTrace();
		}
        
        IProjectDescription description = null;
		try {
			description = project.getDescription();
		} catch (CoreException e) {
			e.printStackTrace();
		}
        description.setNatureIds(new String[] {JavaCore.NATURE_ID});
        try {
			project.setDescription(description, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
        
        IJavaProject javaProject = JavaCore.create(project);
        
        // JRE lib
        try {
        	//get default jre lib
        	IClasspathEntry[] jreLib = PreferenceConstants.getDefaultJRELibrary();
        	// 获取原来的build path
        	IClasspathEntry[] oldClasspathEntries = javaProject.getRawClasspath();
        	List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
        	list.addAll(Arrays.asList(jreLib));
        	list.addAll(Arrays.asList(oldClasspathEntries));
        	
        	javaProject.setRawClasspath(list.toArray(new IClasspathEntry[list.size()]), null);
        } catch(JavaModelException e) {
        	e.printStackTrace();
        	return false;
        }
        
        // output folder
        IFolder binFolder = project.getFolder("bin");
        if(!binFolder.exists())
	        try {
				binFolder.create(true, true, null);
				javaProject.setOutputLocation(binFolder.getFullPath(), null);
	        } catch (CoreException e) {
				e.printStackTrace();
			}
        
        // java builder
        try {
        	IProjectDescription desc = javaProject.getProject().getDescription();
        	ICommand cmd = desc.newCommand();
        	cmd.setBuilderName("org.eclipse.jdt.core.javabuilder");
        	desc.setBuildSpec(new ICommand[] {cmd});
        	desc.setNatureIds(new String[] {"org.eclipse.jdt.core.javanature"});
        	javaProject.getProject().setDescription(desc, null);
        } catch (CoreException e) {
        	e.printStackTrace();
        	return false;
        }
        
        // src Folder
        IFolder srcFolder = javaProject.getProject().getFolder("src");
        if(!srcFolder.exists()) {
        	try {
        		srcFolder.create(true, true, null);
        		IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());
        		
        		IClasspathEntry[] oldClasspathEntries = javaProject.readRawClasspath();
        		
        		List<IClasspathEntry> list = new ArrayList<IClasspathEntry>();
        		list.addAll(Arrays.asList(oldClasspathEntries));
        		list.add(srcClasspathEntry);
        		
        		IClasspathEntry temp = JavaCore.newSourceEntry(new Path("/" + projectName));
        		if(list.contains(temp))
        			list.remove(temp);
        		
        		javaProject.setRawClasspath(list.toArray(new IClasspathEntry[list.size()]), null);
        	} catch (CoreException e) {
        		e.printStackTrace();
        		return false;
        	}
        	
        	// 添加src/tasklist.xml
        	IFile tasklistFile = javaProject.getProject().getFile("src/tasklist.xml");
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("tasklist.xml");
            try {
				tasklistFile.create(in, true, null);
				
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
        }
        
        // ///////////////////////////////////创建新项目///////////////////////////    
//        final IProject project = root.getProject("xyz");    
//            
//        // 设置工程的位置    
//        // 为项目指定存放路径,默认放在当前工作区    
////        IPath projectPath = new Path("g:/myplugIn");  
//        IPath projectPath = project.getLocation();
////        System.out.println(root.getRawLocation());
//        System.out.println(projectPath);
//        IWorkspace workspace = root.getWorkspace(); 
//        System.out.println(workspace);
//        final IProjectDescription description = workspace.newProjectDescription(project.getName());    
//        description.setLocation(projectPath);    
//     
//        // 设置工程标记,即为java工程    
//        String[] javaNature = description.getNatureIds();    
//        String[] newJavaNature = new String[javaNature.length + 1];    
//        System.arraycopy(javaNature, 0, newJavaNature, 0, javaNature.length);    
//        newJavaNature[javaNature.length] = "org.eclipse.jdt.core.javanature"; // 这个标记证明本工程是Java工程    
//        description.setNatureIds(newJavaNature);    
//    
//        // /////////////////////////////    
//        try {    
//            NullProgressMonitor monitor = new NullProgressMonitor();    
//            project.create(description, monitor);    
//            project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 1000));    
//        } catch (CoreException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // 下面代码只在新建向导的情况下才可用    
//        /*  
//         * //创建新项目,WorkspaceModifyOperation位于org.eclipse.ui.ide中  
//         * WorkspaceModifyOperation workspaceModifyOperation = new  
//         * WorkspaceModifyOperation() {  
//         *   
//         * @Override protected void execute(IProgressMonitor monitor) throws  
//         * CoreException, InvocationTargetException, InterruptedException { try  
//         * { monitor.beginTask("", ); project.create(description, monitor);  
//         *   
//         * if(monitor.isCanceled()){ throw new OperationCanceledException(); }  
//         *   
//         * project.open(IResource.BACKGROUND_REFRESH, new  
//         * SubProgressMonitor(monitor, )); } catch (Exception e) {  
//         * e.printStackTrace(); } finally{ monitor.done(); } } };  
//         * //接下来调用IWizard的getContainer().run()方法.  
//         */    
//    
//        // 转化成java工程    
//        IJavaProject javaProject = JavaCore.create(project);    
//        // //////////////////////////////////添加JRE库////////////////////////////    
//        try {    
//            // 获取默认的JRE库    
//            IClasspathEntry[] jreLibrary = PreferenceConstants.getDefaultJRELibrary();    
//            // 获取原来的build path    
//            IClasspathEntry[] oldClasspathEntries = javaProject.getRawClasspath();    
//            List list = new ArrayList();    
//            list.addAll(Arrays.asList(jreLibrary));    
//            list.addAll(Arrays.asList(oldClasspathEntries));    
//    
//            javaProject.setRawClasspath((IClasspathEntry[]) list.toArray(new IClasspathEntry[list.size()]), null);    
//        } catch (JavaModelException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // //////////////////////////////////创建输出路径/////////////////////////////    
//        IFolder binFolder = javaProject.getProject().getFolder("bin");    
//        try {    
//            binFolder.create(true, true, null);    
//            javaProject.setOutputLocation(binFolder.getFullPath(), null);    
//        } catch (CoreException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // /////////////////////////设置Java生成器///////////////////////    
//        try {    
//            IProjectDescription description2 = javaProject.getProject().getDescription();    
//            ICommand command = description2.newCommand();    
//            command.setBuilderName("org.eclipse.jdt.core.javabuilder");    
//            description2.setBuildSpec(new ICommand[] { command });    
//            description2.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature" });    
//            javaProject.getProject().setDescription(description2, null);    
//        } catch (CoreException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // /////////////////////////////创建源代码文件夹//////////////////////////    
//        // ///////////源文件夹和文件夹相似,只是使用PackageFragmentRoot进行了封装////////    
//        IFolder srcFolder = javaProject.getProject().getFolder("src");    
//        try {    
//            srcFolder.create(true, true, null);    
//            // this.createFolder(srcFolder);    
//            // 创建SourceLibrary    
//            IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());    
//    
//            // 得到旧的build path    
//            IClasspathEntry[] oldClasspathEntries = javaProject.readRawClasspath();    
//    
//            // 添加新的    
//            List list = new ArrayList();    
//            list.addAll(Arrays.asList(oldClasspathEntries));    
//            list.add(srcClasspathEntry);    
//    
//            // 原来存在一个与工程名相同的源文件夹,必须先删除    
//            IClasspathEntry temp = JavaCore.newSourceEntry(new Path("/xyz"));    
//            if (list.contains(temp)) {    
//                list.remove(temp);    
//            }    
//    
//            System.out.println(list.size());    
//    
//            javaProject.setRawClasspath((IClasspathEntry[]) list.toArray(new IClasspathEntry[list.size()]), null);    
//        } catch (CoreException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // ///////////////////////////////创建包//////////////////////////    
//        // IPackageFragmentRoot packageFragmentRoot = javaProject.getPackageFragmentRoot(javaProject.getResource());    
//        //此处得到的src目录只读    
//    
//        try {    
//            // 先找指定的源文件夹所在的IPackageFragmentRoot    
//            IPackageFragmentRoot packageFragmentRoot = javaProject.findPackageFragmentRoot(new Path("/xyz/src"));    
//            // 根据IPackageFragmentRoot创建IPackageFragment,IPackageFragment就是包了    
//            IPackageFragment packageFragment = packageFragmentRoot.createPackageFragment("com.aptech.plugin", true, null);    
//    
//        // //////////////////////////////////创建Java文件////////////////////////    
//            String javaCode = "package com.aptech.plugin;public class HelloWorld{public static void main(String[] args){System.out.println(\"中华人民共和国\");}}";    
//            packageFragment.createCompilationUnit("HelloWorld.java", javaCode, true, new NullProgressMonitor());    
//    
//        } catch (JavaModelException e) {    
//            e.printStackTrace();    
//        } catch (Exception e) {    
//            e.printStackTrace();    
//        }    
		
		return true;
//	-----------------------------------------------------	
//		
//		final String containerName = page.getContainerName();
//		final String fileName = page.getFileName();
//		IRunnableWithProgress op = new IRunnableWithProgress() {
//			public void run(IProgressMonitor monitor) throws InvocationTargetException {
//				try {
//					doFinish(containerName, fileName, monitor);
//				} catch (CoreException e) {
//					throw new InvocationTargetException(e);
//				} finally {
//					monitor.done();
//				}
//			}
//		};
//		try {
//			getContainer().run(true, false, op);
//		} catch (InterruptedException e) {
//			return false;
//		} catch (InvocationTargetException e) {
//			Throwable realException = e.getTargetException();
//			MessageDialog.openError(getShell(), "Error", realException.getMessage());
//			return false;
//		}
//		return true;
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}