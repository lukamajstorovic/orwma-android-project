package hr.ferit.lukamajstorovic.projektmob

data class VacantRoom(
    var id: String = "",
    var roomNumber: String = "",
    var occupantName: String = "",
    var occupantOIB: String = "",
    var startDate: String = "",
    var endDate: String = "",
)
