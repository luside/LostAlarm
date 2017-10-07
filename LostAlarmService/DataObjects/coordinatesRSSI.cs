using Microsoft.Azure.Mobile.Server;

namespace LostAlarmService.DataObjects
{
    public class CoordinatesRSSI : EntityData
    {
        public float XCoordinates { get; set; }

        public float YCoordinates { get; set; }

        public int FingerPrintId { get; set; }

        public string HashString { get; set; }


        public bool Complete { get; set; }
    }
}