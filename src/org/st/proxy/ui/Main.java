package org.st.proxy.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.st.proxy.bean.ProxyBean;
import org.st.proxy.service.StringService;
import org.st.proxy.utils.HttpUtils;

/**
 * main ui 
 * @author shituo
 *
 */
public class Main extends JFrame {
	private static final long serialVersionUID = 1L;
	private StringService service = new StringService();
	private JButton button;
	private JTextArea textArea;
	private ExecutorService executor;
	private Integer taskCount;// 任务总数

	// init ui
	public Main() {
		this.setTitle("程序");
		this.setSize(800, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(2, 1));
		textArea = new JTextArea();
		textArea.setSize(400, 100);
		this.add(new JScrollPane(textArea));
		initDownPanel();
		this.setVisible(true);
		executor = Executors.newFixedThreadPool(30);
	}

	// init second panel
	private void initDownPanel() {
		JPanel downPanel = new JPanel(new BorderLayout());
		DefaultListModel<String> fruitsName = new DefaultListModel<>();

		fruitsName.addElement("http://www.kuaidaili.com");
		fruitsName.addElement("Grapes");
		fruitsName.addElement("Mango");
		fruitsName.addElement("Peer");

		JList<String> fruitList = new JList<>(fruitsName);
		fruitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// fruitList.setSelectedIndex(0);
		JScrollPane fruitListScrollPane = new JScrollPane(fruitList);
		downPanel.add(fruitListScrollPane);

		JPanel buttonPanel = new JPanel(new FlowLayout());
		button = new JButton("提交");
		button.addActionListener(new ButtonAction());
		buttonPanel.add(button);
		downPanel.add(buttonPanel, BorderLayout.EAST);
		this.add(downPanel);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
					(LookAndFeel) Class.forName("com.sun.java.swing.plaf.windows.WindowsLookAndFeel").newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		new Main();
	}

	private class ButtonAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			List<ProxyBean> list = service.transStringToProxy(textArea.getText());
			textArea.setText("开始工作... ...");
			button.setEnabled(false);
			taskCount = list.size();
			System.out.println(taskCount);
			for (ProxyBean proxyBean : list) {
				executor.submit(new Task(proxyBean));
			}
		}
	}

	private synchronized void update(String content) {
		textArea.setText(textArea.getText() + "\r\n" + content);
	}

	private synchronized void times() {
		taskCount--;
		System.out.println(taskCount);
		if (taskCount <= 0) {
			taskCount = 0;
			textArea.setText(textArea.getText() + "\r\n任务结束... ...");
			button.setEnabled(true);
		}
	}

	private class Task implements Runnable {
		private ProxyBean proxyBean;

		public Task(ProxyBean proxyBean) {
			this.proxyBean = proxyBean;
		}

		@Override
		public void run() {
			try {
				HttpUtils.httpGet("http://note.youdao.com", proxyBean.getUrl(), proxyBean.getPort());
				System.out.println(proxyBean + " yes");
				update(proxyBean + "");
			} catch (Exception e1) {
				System.out.println(proxyBean + " no");
				// e1.printStackTrace();
			} finally {
				times();
			}
		}

	}
}
