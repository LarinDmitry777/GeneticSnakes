import java.io.FileOutputStream
import java.io.ObjectOutputStream

fun main() {
    val alg = Algorithm()
    val objectOutputStream = ObjectOutputStream(
        FileOutputStream("alg.out")!!
    )
    objectOutputStream.writeObject(alg)
    objectOutputStream.close()
}