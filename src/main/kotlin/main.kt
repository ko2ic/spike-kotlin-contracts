import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@ExperimentalContracts
fun main(args: Array<String>) {
    process1(DataDto("hello!"))

    process2(DataDto("hello!"))

    process3()

    Test().process1(DataDto("test"))
}

data class DataDto(val str: String)

class Test{

    @ExperimentalContracts
    fun process1(dto: DataDto?) {
        validate(dto)
        println(dto.str) // コンパイルエラーなし
    }

    @ExperimentalContracts
    fun validate(dto : DataDto?) {
        contract {
            returns() implies (dto != null)
        }
        if (dto == null) {
            throw IllegalArgumentException()
        }
    }

}

@ExperimentalContracts
private fun process1(dto: DataDto?) {
    validate(dto)
    println(dto.str) // コンパイルエラーなし
}

@ExperimentalContracts
private fun validate(dto : DataDto?) {
    contract {
        returns() implies (dto != null)
    }
    if (dto == null) {
        throw IllegalArgumentException()
    }
}

@ExperimentalContracts
private fun process2(dto: Any) {
    if (isDataDto(dto)){
        println(dto.str) // コンパイルエラーなし
    }
//    println(dto.arg) // コンパイルエラー
}

@ExperimentalContracts
fun isDataDto(dto: Any): Boolean {
    contract {
        returns(true) implies (dto is DataDto)
    }
    return dto is DataDto
}

//@ExperimentalContracts
//private fun process3(dto: DataDto) {
//    val result = str(dto)
//    print(result.length)
//}
//
//@ExperimentalContracts
//fun str(dto: DataDto?): String {
//    contract {
//        returns(null) implies (dto != null )
//    }
//    return if(dto?.str.isNullOrBlank()){
//        "default value"
//    } else {
//        dto!!.str
//    }
//}

@ExperimentalContracts
inline fun <R> run(block: () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }
    return block()
}

@ExperimentalContracts
fun process3() {
    val i: Int
    run {
        i = 1 // callsInPlaceでEXACTLY_ONCEがないとvalに二度設定できるのでエラーになる。
    }
    println(i) // callsInPlaceでEXACTLY_ONCEがないと設定してるかわからないのでエラーになる。
}