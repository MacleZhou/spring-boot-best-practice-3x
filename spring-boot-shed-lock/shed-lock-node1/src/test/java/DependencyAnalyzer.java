import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import java.util.Collection;

public class DependencyAnalyzer {
    public static void main(String[] args) throws Exception {
        // 创建JDepend实例
        JDepend jDepend = new JDepend();

        // 添加分析目录
        jDepend.addDirectory(".");

        // 开始分析
        Collection<JavaPackage> packages = jDepend.analyze();

        // 输出分析结果
        String result = "";
        for(JavaPackage pkg : packages){
            result += "\n包名：" + pkg.getName() + "；抽象度:" + pkg.abstractness() + "; 不稳定性:" + pkg.instability() + "; 距离: " + pkg.distance();
        }
        System.out.println(result);
    }
}
