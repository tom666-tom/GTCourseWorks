using System.Collections;
using UnityEngine;

public class CameraFollow : MonoBehaviour
{

public Transform target;
    public float distanceH = 7f;
    public float distanceV = 4f;
    void LateUpdate()
   {
       Vector3 nextpos = target.forward * -1 * distanceH + target.up * distanceV + target.position;
 
       this.transform.position = nextpos;
 
       this.transform.LookAt(target);
   }
    
}