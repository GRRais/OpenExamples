package ru.rayanis.stroyka.utils

object ImagePicker {
    const val MAX_IMAGE_COUNT = 3
    getImages() {
        val options = Options().apply{
            ratio = Ratio.RATIO_AUTO                                    //Image/video capture ratio
            count = 1                                                   //Number of images to restrict selection count
            spanCount = 4                                               //Number for columns in grid
            path = "Pix/Camera"                                         //Custom Path For media Storage
            isFrontFacing = false                                       //Front Facing camera on start
            videoDurationLimitInSeconds = 10                            //Duration for video recording
            mode = Mode.All                                             //Option to select only pictures or videos or both
            flash = Flash.Auto                                          //Option to select flash type
            preSelectedUrls = ArrayList<Uri>()                          //Pre selected Image Urls
        }
    }
}