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
		// ��ȡ������    
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
        	// ��ȡԭ����build path
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
        	
        	// ���src/tasklist.xml
        	IFile tasklistFile = javaProject.getProject().getFile("src/tasklist.xml");
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("tasklist.xml");
            try {
				tasklistFile.create(in, true, null);
				
			} catch (CoreException e) {
				e.printStackTrace();
				return false;
			}
        }
        
        // ///////////////////////////////////��������Ŀ///////////////////////////    
//        final IProject project = root.getProject("xyz");    
//            
//        // ���ù��̵�λ��    
//        // Ϊ��Ŀָ�����·��,Ĭ�Ϸ��ڵ�ǰ������    
////        IPath projectPath = new Path("g:/myplugIn");  
//        IPath projectPath = project.getLocation();
////        System.out.println(root.getRawLocation());
//        System.out.println(projectPath);
//        IWorkspace workspace = root.getWorkspace(); 
//        System.out.println(workspace);
//        final IProjectDescription description = workspace.newProjectDescription(project.getName());    
//        description.setLocation(projectPath);    
//     
//        // ���ù��̱��,��Ϊjava����    
//        String[] javaNature = description.getNatureIds();    
//        String[] newJavaNature = new String[javaNature.length + 1];    
//        System.arraycopy(javaNature, 0, newJavaNature, 0, javaNature.length);    
//        newJavaNature[javaNature.length] = "org.eclipse.jdt.core.javanature"; // ������֤����������Java����    
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
//        // �������ֻ���½��򵼵�����²ſ���    
//        /*  
//         * //��������Ŀ,WorkspaceModifyOperationλ��org.eclipse.ui.ide��  
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
//         * //����������IWizard��getContainer().run()����.  
//         */    
//    
//        // ת����java����    
//        IJavaProject javaProject = JavaCore.create(project);    
//        // //////////////////////////////////���JRE��////////////////////////////    
//        try {    
//            // ��ȡĬ�ϵ�JRE��    
//            IClasspathEntry[] jreLibrary = PreferenceConstants.getDefaultJRELibrary();    
//            // ��ȡԭ����build path    
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
//        // //////////////////////////////////�������·��/////////////////////////////    
//        IFolder binFolder = javaProject.getProject().getFolder("bin");    
//        try {    
//            binFolder.create(true, true, null);    
//            javaProject.setOutputLocation(binFolder.getFullPath(), null);    
//        } catch (CoreException e) {    
//            e.printStackTrace();    
//        }    
//    
//        // /////////////////////////����Java������///////////////////////    
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
//        // /////////////////////////////����Դ�����ļ���//////////////////////////    
//        // ///////////Դ�ļ��к��ļ�������,ֻ��ʹ��PackageFragmentRoot�����˷�װ////////    
//        IFolder srcFolder = javaProject.getProject().getFolder("src");    
//        try {    
//            srcFolder.create(true, true, null);    
//            // this.createFolder(srcFolder);    
//            // ����SourceLibrary    
//            IClasspathEntry srcClasspathEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());    
//    
//            // �õ��ɵ�build path    
//            IClasspathEntry[] oldClasspathEntries = javaProject.readRawClasspath();    
//    
//            // ����µ�    
//            List list = new ArrayList();    
//            list.addAll(Arrays.asList(oldClasspathEntries));    
//            list.add(srcClasspathEntry);    
//    
//            // ԭ������һ���빤������ͬ��Դ�ļ���,������ɾ��    
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
//        // ///////////////////////////////������//////////////////////////    
//        // IPackageFragmentRoot packageFragmentRoot = javaProject.getPackageFragmentRoot(javaProject.getResource());    
//        //�˴��õ���srcĿ¼ֻ��    
//    
//        try {    
//            // ����ָ����Դ�ļ������ڵ�IPackageFragmentRoot    
//            IPackageFragmentRoot packageFragmentRoot = javaProject.findPackageFragmentRoot(new Path("/xyz/src"));    
//            // ����IPackageFragmentRoot����IPackageFragment,IPackageFragment���ǰ���    
//            IPackageFragment packageFragment = packageFragmentRoot.createPackageFragment("com.aptech.plugin", true, null);    
//    
//        // //////////////////////////////////����Java�ļ�////////////////////////    
//            String javaCode = "package com.aptech.plugin;public class HelloWorld{public static void main(String[] args){System.out.println(\"�л����񹲺͹�\");}}";    
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