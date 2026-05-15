using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class JumpToCredit : MonoBehaviour
{
    public void GotoCredit()
    {
        SceneManager.LoadScene("Credit");
    }
}
