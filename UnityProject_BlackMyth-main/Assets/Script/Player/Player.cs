using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public class Player : MonoBehaviour
{
    public string targetTag = "1";
    public Text pickupText;
    public GameObject lj;

    void Update()
    {
     
        if (Input.GetKeyDown(KeyCode.F) && lj!=null)
        {
         
            Destroy(lj.gameObject);
            pickupText.gameObject.SetActive(false);
        }
    }

    void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag(targetTag))
        {
            lj = other.gameObject;
            pickupText.gameObject.SetActive(true);
        }
    }

    void OnTriggerExit(Collider other)
    {
        if (other.CompareTag(targetTag))
        {
            lj = null;
            pickupText.gameObject.SetActive(false);
        }
    }

}
