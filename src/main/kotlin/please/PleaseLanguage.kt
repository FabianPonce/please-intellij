package please

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.lang.Language
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import com.intellij.psi.FileViewProvider
import java.nio.file.Path


val PLEASE_ICON = IconLoader.getIcon("/icons/please.png", PleaseFileType.javaClass)
object PleaseLanguage : Language("Please")

object PleaseFileType : LanguageFileType(PleaseLanguage) {
    override fun getIcon() = PLEASE_ICON

    override fun getName() = "Please"

    override fun getDefaultExtension() = ".plz"

    override fun getDescription() = ""
}

class PleaseFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, PleaseLanguage) {
    private var pkg = ""
    override fun getFileType(): FileType {
        return PleaseFileType
    }

    override fun toString(): String {
        return "Please File"
    }

    fun getPleasePackage() : String {
        if (pkg != "") {
            return pkg
        }
        var dir = Path.of(virtualFile.path).parent
        val path = mutableListOf<String>()
        while(true) {
            if(dir == null){
                throw RuntimeException("Could not locate .plzconfig")
            }

            val dirFile = dir.toFile()
            if (dir.toFile().list()!!.find { it == ".plzconfig" } != null) {
                pkg = path.joinToString("/")
                return pkg
            } else {
                path.add(0, dirFile.name)
                dir = dir.parent
            }
        }
    }
}
